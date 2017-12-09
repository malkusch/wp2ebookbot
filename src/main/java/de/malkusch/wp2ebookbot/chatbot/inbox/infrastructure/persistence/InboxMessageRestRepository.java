package de.malkusch.wp2ebookbot.chatbot.inbox.infrastructure.persistence;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriTemplate;

import com.google.gson.Gson;

import de.malkusch.wp2ebookbot.chatbot.inbox.model.ArticleId;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.Author;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.CommentId;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.InboxMessage;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.InboxMessageContext;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.InboxMessageId;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.InboxMessageRepository;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.Title;
import de.malkusch.wp2ebookbot.shared.infrastructure.StreamUtil;
import de.malkusch.wp2ebookbot.shared.infrastructure.reddit.RedditRestTemplate;

@Service
final class InboxMessageRestRepository extends InboxMessageRepository {

    private final RedditRestTemplate restTemplate;

    InboxMessageRestRepository(RedditRestTemplate restTemplate,
            @Value("${reddit.inbox.unread.uri}") String unreadEndpoint,
            @Value("${reddit.oauthBaseUri}") String oauthBaseUri,
            @Value("${reddit.inbox.context.path}") UriTemplate contextPath,
            @Value("${reddit.wp.subreddit}") String wpSubreddit,
            @Value("${reddit.inbox.markRead.uri}") String markReadEndpoint) {

        this.restTemplate = restTemplate;
        this.unreadEndpoint = unreadEndpoint;
        this.oauthBaseUri = oauthBaseUri;
        this.contextPath = contextPath;
        this.wpSubreddit = wpSubreddit;
        this.markReadEndpoint = markReadEndpoint;
    }

    private final String unreadEndpoint;

    @Override
    public Collection<InboxMessage> fetchNewMessages() throws IOException {
        try {
            Unread unread = restTemplate.getForObject(unreadEndpoint, Unread.class);
            Collection<InboxMessage> newMessages = Arrays.stream(unread.data.children).filter(this::isWPComment)
                    .map(IncompleteInboxMessage::new).map(this::fetchContext).flatMap(StreamUtil::optionalStream)
                    .collect(Collectors.toList());
            markOtherMessagesRead(unread, newMessages);
            return newMessages;

        } catch (RestClientException e) {
            throw new IOException(e);
        }
    }

    private void markOtherMessagesRead(Unread unread, Collection<InboxMessage> newMessages) {
        Arrays.stream(unread.data.children)
                .filter(t -> !newMessages.stream().anyMatch(m -> t.data.name.equals(m.id().commentId().fullname())))
                .map(t -> t.data.name).forEach(this::markRead);
    }

    private final String oauthBaseUri;
    private final UriTemplate contextPath;
    private static final int CONTEXT_DEPTH = 10;

    private Optional<InboxMessage> fetchContext(IncompleteInboxMessage incomplete) throws RestClientException {
        Map<String, String> contextParameters = contextPath.match(incomplete.context);
        contextParameters.put("context", Integer.toString(CONTEXT_DEPTH));
        String contextEndpoint = oauthBaseUri + contextPath.expand(contextParameters);
        String rawContextResponse = restTemplate.getForObject(contextEndpoint, String.class);
        ContextThing[] contextResponse = fixContextResponse(rawContextResponse);

        Optional<Title> title = findTitle(contextResponse);
        Optional<InboxMessageContext> context = buildContextFor(incomplete.id.commentId().articleId(),
                incomplete.id.commentId().fullname(), contextResponse);

        return title.map(t -> hydrateInboxMessage(incomplete.id, incomplete.author, t, incomplete.message, context));
    }

    private final Gson gson = new Gson();

    private ContextThing[] fixContextResponse(String response) {
        String fixed = response.replaceAll("\"replies\" *: *\"[^\"]*\"", "\"replies\" : null");
        return gson.fromJson(fixed, ContextThing[].class);
    }

    private static Optional<InboxMessageContext> buildContextFor(ArticleId articleId, String fullname,
            ContextThing[] contextResponse) {

        Optional<String> contextFullname = Arrays.stream(contextResponse).flatMap(ContextThing::flatThings)
                .filter(t -> StringUtils.equals(t.data.name, fullname)).map(t -> t.data.parent_id).findAny();

        Optional<ContextThing> contextThing = contextFullname
                .flatMap(n -> Arrays.stream(contextResponse).flatMap(ContextThing::flatThings)
                        .filter(t -> StringUtils.equals(t.data.name, n) && t.kind.equals("t1")).findAny());

        Optional<InboxMessageContext> moreContext = contextThing
                .flatMap(t -> buildContextFor(articleId, t.data.name, contextResponse));

        return contextThing.map(t -> hydrateContext(new Author(t.data.author), new CommentId(articleId, t.data.id),
                t.data.body, moreContext));
    }

    private static Optional<Title> findTitle(ContextThing[] contextResponse) {
        return Arrays.stream(contextResponse).flatMap(ContextThing::flatThings).filter(t -> t.kind.equals("t3"))
                .map(t -> new Title(t.data.title)).findAny();
    }

    private static class ContextThing {

        private String kind;
        private Data data;

        private static class Data {

            private ContextThing[] children;
            private String title;
            private String author;
            private String id;
            private String parent_id;
            private String name;
            private String body;
            private ContextThing replies;

        }

        private Stream<ContextThing> flatThings() {
            Stream<ContextThing> things = Stream.of(this);
            if (data.children != null) {
                things = Stream.concat(things, Arrays.stream(data.children).flatMap(ContextThing::flatThings));
            }
            if (data.replies != null) {
                things = Stream.concat(things, data.replies.flatThings());
            }
            return things;
        }

    }

    private final String wpSubreddit;

    private boolean isWPComment(Unread.UnreadData.Thing thing) {
        return thing.kind.equals("t1") && thing.data.was_comment && thing.data.subreddit.equals(wpSubreddit)
                && contextPath.matches(thing.data.context);
    }

    private class IncompleteInboxMessage {

        private final InboxMessageId id;
        private final Author author;
        private final String message;
        private final String context;

        IncompleteInboxMessage(Unread.UnreadData.Thing thing) {
            this.author = new Author(thing.data.author);
            this.message = thing.data.body;
            this.context = thing.data.context;

            Map<String, String> contextParameters = contextPath.match(context);
            id = new InboxMessageId(new CommentId(new ArticleId(contextParameters.get("articleId")), thing.data.id));
        }

    }

    private static class Unread {

        private UnreadData data;

        private static class UnreadData {

            private Thing[] children;

            private static class Thing {

                private String kind;
                private ThingData data;

                private static class ThingData {

                    private String id;
                    private String name;
                    private String author;
                    private String context;
                    private String body;
                    private String subreddit;
                    private boolean was_comment;

                }

            }

        }

    }

    @Override
    public void markRead(InboxMessageId id) throws IOException {
        try {
            Objects.requireNonNull(id);
            markRead(id.commentId().fullname());

        } catch (RestClientException e) {
            throw new IOException(e);
        }
    }

    private final String markReadEndpoint;

    private void markRead(String fullname) throws RestClientException {
        restTemplate.postForObject(markReadEndpoint, Collections.singletonMap("id", fullname), String.class);
    }

}

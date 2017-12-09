package de.malkusch.wp2ebookbot.chatbot.outbox.infrastructure.persistence;

import static java.util.Arrays.stream;
import static java.util.stream.Stream.concat;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import de.malkusch.wp2ebookbot.chatbot.outbox.model.Comment;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.CommentId;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.Title;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.Votes;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.Words;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.WritingPrompSpecification;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.WritingPrompt;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.WritingPromptId;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.WritingPromptRepository;
import de.malkusch.wp2ebookbot.shared.infrastructure.StreamUtil;
import de.malkusch.wp2ebookbot.shared.infrastructure.reddit.RedditRestTemplate;

@Service
final class WritingPromptRestRepository extends WritingPromptRepository {

    private final RedditRestTemplate restTemplate;

    public WritingPromptRestRepository(@Value("${reddit.comments.uri}") String commentsEndpoint,
            @Value("${reddit.wp.uri}") String wpEndpoint, RedditRestTemplate restTemplate) {

        this.commentsEndpoint = commentsEndpoint;
        this.wpEndpoint = wpEndpoint;
        this.restTemplate = restTemplate;
    }

    @Override
    public WritingPrompt[] findEligibleWritingPromptsSince(Instant since, WritingPrompSpecification hint)
            throws IOException {

        try {
            IncompleteWritingPrompt[] incomplete = newEligibleWritingPrompts(since, hint);
            WritingPrompt[] newPrompts = stream(incomplete).map(wp -> this.withTopComment(wp, hint))
                    .flatMap(StreamUtil::optionalStream).toArray(WritingPrompt[]::new);
            return newPrompts;

        } catch (RestClientException e) {
            throw new IOException(e);
        }
    }

    private Optional<WritingPrompt> withTopComment(IncompleteWritingPrompt incomplete, WritingPrompSpecification hint) {
        return findEligibleTopComment(incomplete, hint)
                .map(c -> hydrateWritingPrompt(incomplete.id, incomplete.title, incomplete.votes, c));
    }

    private final String commentsEndpoint;

    private Optional<Comment> findEligibleTopComment(IncompleteWritingPrompt incomplete,
            WritingPrompSpecification hint) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("articleId", incomplete.id);
        parameters.put("truncate", 5);
        parameters.put("depth", 1);
        parameters.put("commentId", null);
        Thing[] comments = restTemplate.getForObject(commentsEndpoint, Thing[].class, parameters);
        return stream(comments).flatMap(Thing::flatThings).filter(t -> t.kind.equals("t1"))
                .filter(t -> t.data.stickied == false && t.data.depth == 0).map(this::toComment)
                .filter(hint::isSatisfiedBy).findFirst();

    }

    private Comment toComment(Thing thing) {
        CommentId id = new CommentId(thing.data.id);
        Votes votes = new Votes(thing.data.score);
        Words words = words(thing.data.body);
        return hydrateComment(id, votes, words);
    }

    private static Words words(String body) {
        if (body == null) {
            return new Words(0);
        }
        return new Words(body.split("\\s+").length);
    }

    private static class Thing {

        private String kind;
        private Data data;

        private static class Data {

            private String id;
            private int depth;
            private int score;
            private String body;
            private boolean stickied;
            private Thing[] children;

        }

        private Stream<Thing> flatThings() {
            if (data.children == null) {
                return Stream.of(this);
            }
            return concat(Stream.of(this), stream(data.children).flatMap(Thing::flatThings));
        }

    }

    private final String wpEndpoint;

    private IncompleteWritingPrompt[] newEligibleWritingPrompts(Instant since, WritingPrompSpecification wpSpec) {
        NewWritingPrompts writingPrompts = restTemplate.getForObject(wpEndpoint, NewWritingPrompts.class);
        return stream(writingPrompts.data.children).filter(t -> t.data.num_comments > 2)
                .map(IncompleteWritingPrompt::new).filter(wp -> wp.isEligible(since, wpSpec))
                .toArray(IncompleteWritingPrompt[]::new);
    }

    private static class IncompleteWritingPrompt {

        private final WritingPromptId id;
        private final Title title;
        private final Votes votes;
        private final Instant created;

        private IncompleteWritingPrompt(NewWritingPrompts.Data.Thing thing) {
            id = new WritingPromptId(thing.data.id);
            title = new Title(thing.data.title);
            votes = new Votes(thing.data.score);
            created = Instant.ofEpochSecond(thing.data.created_utc);
        }

        private boolean isEligible(Instant since, WritingPrompSpecification wpSpec) {
            return created.isAfter(since) && title.isWritingPrompt() && wpSpec.isSatisfiedByWritingPromptVotes(votes);
        }

    }

    private static class NewWritingPrompts {

        private Data data;

        private static class Data {

            private Thing[] children;

            private static class Thing {

                private ThingData data;

                private static class ThingData {

                    private String id;
                    private int created_utc;
                    private String title;
                    private int num_comments;
                    private int score;

                }

            }

        }

    }

}

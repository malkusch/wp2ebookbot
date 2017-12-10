package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;

import de.malkusch.wp2ebookbot.shared.infrastructure.reddit.RedditRestTemplate;

@Service
@Profile("test")
final class InboxTestService {

    @Autowired
    private RedditRestTemplate restTemplate;

    InboxMessageId anyUnreadMessage(String subreddit) {
        InboxMessageId anyMessage = anyInboxMessage(subreddit);
        unread(anyMessage.commentId.fullname());
        return anyMessage;
    }

    private static final String INBOX_ENDPOINT = "https://oauth.reddit.com/message/inbox";

    @Value("${reddit.inbox.context.path}")
    private UriTemplate contextPath;

    private InboxMessageId anyInboxMessage(String subreddit) {
        Inbox inbox = restTemplate.getForObject(INBOX_ENDPOINT, Inbox.class);
        return Arrays.stream(inbox.data.children)
                .filter(t -> t.kind.equals("t1") && StringUtils.equals(t.data.subreddit, subreddit)).map(this::toId)
                .findAny().get();
    }

    private InboxMessageId toId(Inbox.InboxData.Thing thing) {
        Map<String, String> contextParameters = contextPath.match(thing.data.context);
        String articleId = contextParameters.get("articleId");
        return new InboxMessageId(new CommentId(new ArticleId(articleId), thing.data.id));
    }

    private static class Inbox {

        private InboxData data;

        private static class InboxData {

            private Thing[] children;

            private static class Thing {

                private String kind;
                private ThingData data;

                private static class ThingData {

                    private String id;
                    private String context;
                    private String subreddit;

                }

            }

        }

    }

    private static final String UNREAD_ENDPOINT = "https://oauth.reddit.com/api/unread_message";

    private void unread(String fullname) {
        restTemplate.postForObject(UNREAD_ENDPOINT, Collections.singletonMap("id", fullname), String.class);
    }

}

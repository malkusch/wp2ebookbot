package de.malkusch.wp2ebookbot.chatbot.outbox.infrastructure.persistence.answer;

import static java.util.Arrays.stream;
import static java.util.stream.Stream.concat;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.malkusch.wp2ebookbot.chatbot.outbox.model.CommentId;
import de.malkusch.wp2ebookbot.shared.infrastructure.reddit.RedditRestTemplate;

@Service
public final class AnswerableCommentService {

    @Autowired
    private RedditRestTemplate restTemplate;

    public CommentId findAnswerableCommentId() {
        String articleId = articleIdWithComments();
        String commentId = anyCommentId(articleId);
        return new CommentId(commentId);
    }

    @Value("${reddit.comments.uri}")
    private String commentsEndpoint;

    private String anyCommentId(String articleId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("articleId", articleId);
        parameters.put("truncate", 5);
        parameters.put("depth", 1);
        parameters.put("commentId", null);
        Thing[] comments = restTemplate.getForObject(commentsEndpoint, Thing[].class, parameters);

        return stream(comments).flatMap(Thing::flatThings)
                .filter(t -> t.kind.equals("t1") && !StringUtils.equals(t.data.body, "[deleted]")).map(t -> t.data.id)
                .findAny().orElseThrow(IllegalStateException::new);
    }

    private static class Thing {

        private String kind;
        private Data data;

        private static class Data {

            private String id;
            private Thing[] children;
            private String body;

        }

        private Stream<Thing> flatThings() {
            if (data.children == null) {
                return Stream.of(this);
            }
            return concat(Stream.of(this), stream(data.children).flatMap(Thing::flatThings));
        }

    }

    @Value("${reddit.new.uri}")
    private String newEndpoint;

    private static final String TEST_SUBREDDIT = "test";

    private String articleIdWithComments() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("subreddit", TEST_SUBREDDIT);
        NewArticlesResponse articles = restTemplate.getForObject(newEndpoint, NewArticlesResponse.class, parameters);
        return stream(articles.data.children).filter(t -> t.data.num_comments > 0).map(t -> t.data.id).findAny()
                .orElseThrow(IllegalStateException::new);
    }

    private static class NewArticlesResponse {

        private Data data;

        private static class Data {

            private Thing[] children;

            private static class Thing {

                private ThingData data;

                private static class ThingData {

                    private String id;
                    private int num_comments;

                }

            }

        }

    }

}

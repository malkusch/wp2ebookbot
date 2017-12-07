package de.malkusch.wp2ebookbot.chatbot.outbox.infrastructure.persistence.answer;

import static java.util.Arrays.stream;
import static java.util.stream.Stream.concat;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
        parameters.put("truncate", 1);
        parameters.put("depth", 1);
        parameters.put("commentId", null);
        Thing[] comments = restTemplate.getForObject(commentsEndpoint, Thing[].class, parameters);
        return stream(comments).flatMap(Thing::flatThings).filter(t -> t.kind.equals("t1")).map(t -> t.data.id)
                .findAny().orElseThrow(IllegalStateException::new);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Thing {

        private @JsonProperty String kind;
        private @JsonProperty Data data;

        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class Data {

            private @JsonProperty String id;
            private @JsonProperty Thing[] children;

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

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class NewArticlesResponse {

        private @JsonProperty Data data;

        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class Data {

            private @JsonProperty Thing[] children;

            @JsonIgnoreProperties(ignoreUnknown = true)
            private static class Thing {

                private @JsonProperty ThingData data;

                @JsonIgnoreProperties(ignoreUnknown = true)
                private static class ThingData {

                    private @JsonProperty String id;
                    private @JsonProperty int num_comments;

                }

            }

        }

    }

}

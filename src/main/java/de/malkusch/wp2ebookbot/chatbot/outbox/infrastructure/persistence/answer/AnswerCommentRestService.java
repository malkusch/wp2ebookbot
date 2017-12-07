package de.malkusch.wp2ebookbot.chatbot.outbox.infrastructure.persistence.answer;

import static java.util.Arrays.stream;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.malkusch.wp2ebookbot.chatbot.outbox.model.CommentId;
import de.malkusch.wp2ebookbot.shared.infrastructure.reddit.RedditRestTemplate;

@Service
final class AnswerCommentRestService {

    private final RedditRestTemplate restTemplate;
    private final URI commentEndpoint;

    AnswerCommentRestService(RedditRestTemplate restTemplate, @Value("${reddit.comment.uri}") URI commentEndpoint) {
        this.restTemplate = restTemplate;
        this.commentEndpoint = commentEndpoint;
    }

    Permalink answerComment(CommentId parent, String response) throws IOException {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("thing_id", parent.fullname());
            data.put("text", response);
            data.put("api_type", "json");

            Result result = restTemplate.postForObject(commentEndpoint, data, Result.class);
            checkError(result);
            return new Permalink(result.json.data.things[0].data.permalink);

        } catch (RestClientException e) {
            throw new IOException(e);
        }
    }

    private static void checkError(Result result) throws IOException {
        String error = stream(result.json.errors).flatMap(e -> stream(e)).collect(Collectors.joining("\n"));
        if (error.isEmpty()) {
            return;
        }

        Optional<?> ratelimit = stream(result.json.errors).filter(e -> e[0].equals("RATELIMIT")).findAny();
        if (ratelimit.isPresent()) {
            throw new RateLimitException(error);
        }

        throw new IOException(error);
    }

    static final class Permalink {

        final String link;

        private Permalink(String link) {
            this.link = link;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Result {

        private @JsonProperty JSON json;

        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class JSON {

            private @JsonProperty String[][] errors;
            private @JsonProperty Things data;

            @JsonIgnoreProperties(ignoreUnknown = true)
            private static class Things {

                private @JsonProperty Thing[] things;

                @JsonIgnoreProperties(ignoreUnknown = true)
                private static class Thing {

                    private @JsonProperty String kind;
                    private @JsonProperty Data data;

                    @JsonIgnoreProperties(ignoreUnknown = true)
                    private static class Data {

                        private @JsonProperty String id;
                        private @JsonProperty String permalink;

                    }
                }
            }
        }
    }
}

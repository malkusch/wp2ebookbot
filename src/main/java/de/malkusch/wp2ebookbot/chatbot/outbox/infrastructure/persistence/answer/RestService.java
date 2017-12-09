package de.malkusch.wp2ebookbot.chatbot.outbox.infrastructure.persistence.answer;

import static java.util.Arrays.stream;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import de.malkusch.wp2ebookbot.chatbot.outbox.model.CommentId;
import de.malkusch.wp2ebookbot.shared.infrastructure.reddit.RedditRestTemplate;

@Service
final class RestService {

    private final RedditRestTemplate restTemplate;
    private final URI commentEndpoint;
    private static final Logger LOGGER = LoggerFactory.getLogger(RestService.class);

    RestService(RedditRestTemplate restTemplate, @Value("${reddit.comment.uri}") URI commentEndpoint) {
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
            Permalink link = new Permalink(result.json.data.things[0].data.permalink);
            LOGGER.debug("Answer published: {}", link.link);
            return link;

        } catch (HttpServerErrorException e) {
            throw new RetryableIOException(e);

        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 429) {
                throw new RetryableIOException(e);
            }
            throw new IOException(e);

        } catch (RestClientException e) {
            throw new IOException(e);
        }
    }

    private static void checkError(Result result) throws IOException {
        String error = stream(result.json.errors).flatMap(e -> stream(e)).collect(Collectors.joining("\n"));
        if (error.isEmpty()) {
            return;
        }
        if (containsError(result, "RATELIMIT")) {
            throw new RetryableIOException(error);
        }
        if (containsError(result, "NO_TEXT")) {
            throw new IllegalArgumentException(error);
        }
        if (containsError(result, "TOO_OLD")) {
            throw new IllegalArgumentException(error);
        }
        throw new IOException(error);
    }

    private static boolean containsError(Result result, String error) {
        return stream(result.json.errors).filter(e -> e[0].equals(error)).findAny().isPresent();
    }

    static final class Permalink {

        final String link;

        private Permalink(String link) {
            this.link = link;
        }

    }

    private static class Result {

        private JSON json;

        private static class JSON {

            private String[][] errors;
            private Things data;

            private static class Things {

                private Thing[] things;

                private static class Thing {

                    private Data data;

                    private static class Data {

                        private String permalink;

                    }
                }
            }
        }
    }
}

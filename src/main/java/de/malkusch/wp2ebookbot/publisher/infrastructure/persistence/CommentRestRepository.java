package de.malkusch.wp2ebookbot.publisher.infrastructure.persistence;

import static java.util.Arrays.stream;
import static java.util.stream.Stream.concat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.malkusch.wp2ebookbot.publisher.model.Author;
import de.malkusch.wp2ebookbot.publisher.model.Comment;
import de.malkusch.wp2ebookbot.publisher.model.CommentId;
import de.malkusch.wp2ebookbot.publisher.model.CommentRepository;
import de.malkusch.wp2ebookbot.publisher.model.ThreadTitle;
import de.malkusch.wp2ebookbot.shared.infrastructure.reddit.Authentication;
import de.malkusch.wp2ebookbot.shared.infrastructure.reddit.RedditAuthenticationService;
import de.malkusch.wp2ebookbot.shared.infrastructure.reddit.RedditBotRateLimitService;

@Service
class CommentRestRepository extends CommentRepository {

    private final RedditAuthenticationService authenticationService;
    private final RestTemplate restTemplate;
    private final RedditBotRateLimitService rateLimiter;
    private final String commentsEndpoint;

    CommentRestRepository(RedditAuthenticationService authenticationService, RedditBotRateLimitService rateLimiter,
            RestTemplate restTemplate, @Value("${reddit.comments.uri}") String commentsEndpoint) {

        this.authenticationService = authenticationService;
        this.restTemplate = restTemplate;
        this.rateLimiter = rateLimiter;
        this.commentsEndpoint = commentsEndpoint;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Thing {

        private @JsonProperty String kind;
        private @JsonProperty Data data;

        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class Data {

            private @JsonProperty String id;
            private @JsonProperty String title;
            private @JsonProperty String author;
            private @JsonProperty String body_html;
            private @JsonProperty Thing[] children;
        }

        private boolean matches(String kind, String id) {
            return kind.equals(this.kind) && id.equals(data.id);
        }

        private Stream<Thing> flatThings() {
            if (data.children == null) {
                return Stream.of(this);
            }
            return concat(Stream.of(this), stream(data.children).flatMap(Thing::flatThings));
        }

    }

    @Override
    @Retryable(IOException.class)
    public Optional<Comment> findById(CommentId id) throws IOException {
        rateLimiter.limitRate();

        Authentication authentication = authenticationService.getAuthentication();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authentication.toString());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("articleId", id.articleId());
        parameters.put("commentId", id);
        parameters.put("truncate", 1);
        parameters.put("depth", 1);

        try {
            Thing[] response = restTemplate
                    .exchange(commentsEndpoint, HttpMethod.GET, entity, Thing[].class, parameters).getBody();

            Optional<Thing> apiArticle = stream(response).flatMap(Thing::flatThings)
                    .filter(t -> t.matches("t3", id.articleId().toString())).findAny();

            Optional<Thing> apiComment = stream(response).flatMap(Thing::flatThings)
                    .filter(t -> t.matches("t1", id.toString())).findAny();

            return apiArticle.flatMap(a -> apiComment.map(c -> toComment(a, id, c)));

        } catch (RestClientResponseException e) {
            if (e.getRawStatusCode() == 404) {
                return Optional.empty();
            }
            throw new IOException(e.getResponseBodyAsString(), e);

        } catch (RestClientException e) {
            throw new IOException(e);
        }
    }

    private static Comment toComment(Thing apiArticle, CommentId id, Thing apiComment) {
        ThreadTitle title = new ThreadTitle(apiArticle.data.title);
        Author author = new Author(apiComment.data.author);
        String body = unescapeBody(apiComment.data.body_html);
        return hydrate(id, title, author, body);
    }

    private static String unescapeBody(String body) {
        return StringEscapeUtils.unescapeHtml4(body);
    }

}

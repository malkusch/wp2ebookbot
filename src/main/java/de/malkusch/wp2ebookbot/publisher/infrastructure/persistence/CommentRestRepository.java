package de.malkusch.wp2ebookbot.publisher.infrastructure.persistence;

import static java.util.Arrays.stream;
import static java.util.stream.Stream.concat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import de.malkusch.wp2ebookbot.publisher.model.Author;
import de.malkusch.wp2ebookbot.publisher.model.Comment;
import de.malkusch.wp2ebookbot.publisher.model.CommentId;
import de.malkusch.wp2ebookbot.publisher.model.CommentRepository;
import de.malkusch.wp2ebookbot.publisher.model.ThreadTitle;
import de.malkusch.wp2ebookbot.shared.infrastructure.reddit.RedditRestTemplate;

@Service
class CommentRestRepository extends CommentRepository {

    private final RedditRestTemplate restTemplate;
    private final String commentsEndpoint;

    CommentRestRepository(RedditRestTemplate restTemplate, @Value("${reddit.comments.uri}") String commentsEndpoint) {
        this.restTemplate = restTemplate;
        this.commentsEndpoint = commentsEndpoint;
    }

    private static class Thing {

        private String kind;
        private Data data;

        private static class Data {

            private String id;
            private String title;
            private String author;
            private String body_html;
            private Thing[] children;
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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("articleId", id.articleId());
        parameters.put("commentId", id);
        parameters.put("truncate", 1);
        parameters.put("depth", 1);

        try {
            Thing[] response = restTemplate.getForObject(commentsEndpoint, Thing[].class, parameters);

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
        String body = apiComment.data.body_html;
        return hydrate(id, title, author, body);
    }

}

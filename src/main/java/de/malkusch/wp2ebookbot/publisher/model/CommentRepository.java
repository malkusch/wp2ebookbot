package de.malkusch.wp2ebookbot.publisher.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public abstract class CommentRepository {

    public abstract Optional<Comment> findById(CommentId id) throws IOException;

    protected final Comment hydrate(CommentId id, ThreadTitle title, Author author, InputStream body) {
        return new Comment(id, title, author, body);
    }

}

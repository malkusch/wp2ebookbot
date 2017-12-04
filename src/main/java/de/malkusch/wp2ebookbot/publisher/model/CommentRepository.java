package de.malkusch.wp2ebookbot.publisher.model;

import java.io.IOException;
import java.util.Optional;

public abstract class CommentRepository {

    public abstract Optional<Comment> findById(CommentId id) throws IOException;

    protected final Comment hydrate(CommentId id, PermissionId permissionId, ThreadTitle title, Author author,
            String body) {

        return new Comment(id, permissionId, title, author, body);
    }

}

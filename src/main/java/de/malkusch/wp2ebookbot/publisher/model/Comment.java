package de.malkusch.wp2ebookbot.publisher.model;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;

public final class Comment implements AutoCloseable {

    final CommentId id;
    final PermissionId permissionId;
    final ThreadTitle title;
    final Author author;
    final InputStream body;

    Comment(CommentId id, PermissionId permissionId, ThreadTitle title, Author author, InputStream body) {
        this.id = requireNonNull(id);
        this.permissionId = requireNonNull(permissionId);
        this.title = requireNonNull(title);
        this.author = requireNonNull(author);
        this.body = requireNonNull(body);
    }

    @Override
    public String toString() {
        return title.toString();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Comment) {
            Comment other = (Comment) obj;
            return id.equals(other.id);

        } else {
            return false;
        }
    }

    @Override
    public void close() throws IOException {
        body.close();
    }

}

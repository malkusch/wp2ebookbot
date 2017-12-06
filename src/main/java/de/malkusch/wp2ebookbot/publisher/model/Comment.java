package de.malkusch.wp2ebookbot.publisher.model;

import static java.util.Objects.requireNonNull;

public final class Comment {

    final CommentId id;
    final ThreadTitle title;
    final Author author;
    final String body;

    Comment(CommentId id, ThreadTitle title, Author author, String body) {
        this.id = requireNonNull(id);
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

}

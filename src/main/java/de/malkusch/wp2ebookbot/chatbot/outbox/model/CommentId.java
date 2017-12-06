package de.malkusch.wp2ebookbot.chatbot.outbox.model;

import static java.util.Objects.requireNonNull;

public final class CommentId {

    private final String id;

    public CommentId(String id) {
        if (requireNonNull(id.isEmpty())) {
            throw new IllegalArgumentException("Comment id must not be empty");
        }
        this.id = id;
    }

    public String fullname() {
        return "t1_" + id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CommentId) {
            CommentId other = (CommentId) obj;
            return id.equals(other.id);

        } else {
            return false;
        }
    }

}

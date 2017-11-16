package de.malkusch.wp2ebookbot.chatbot.outbox.model;

import static java.util.Objects.requireNonNull;

public final class Permission {

    final CommentId commentId;

    public Permission(CommentId id) {
        this.commentId = requireNonNull(id);
    }

    @Override
    public String toString() {
        return commentId.toString();
    }

    @Override
    public int hashCode() {
        return commentId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Permission) {
            Permission other = (Permission) obj;
            return commentId.equals(other.commentId);

        } else {
            return false;
        }
    }

}

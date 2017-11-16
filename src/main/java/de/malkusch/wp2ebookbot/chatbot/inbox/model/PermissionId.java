package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Objects.requireNonNull;

public final class PermissionId {

    final CommentId commentId;

    public PermissionId(CommentId id) {
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
        if (obj instanceof PermissionId) {
            PermissionId other = (PermissionId) obj;
            return commentId.equals(other.commentId);

        } else {
            return false;
        }
    }

}

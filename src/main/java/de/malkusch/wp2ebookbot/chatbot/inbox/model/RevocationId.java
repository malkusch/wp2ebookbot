package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Objects.requireNonNull;

public final class RevocationId {

    final CommentId commentId;

    public RevocationId(CommentId id) {
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
        if (obj instanceof RevocationId) {
            RevocationId other = (RevocationId) obj;
            return commentId.equals(other.commentId);

        } else {
            return false;
        }
    }

}

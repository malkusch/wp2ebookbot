package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Objects.requireNonNull;

public final class InboxMessageId {

    final CommentId commentId;

    public InboxMessageId(CommentId id) {
        this.commentId = requireNonNull(id);
    }

    public CommentId commentId() {
        return commentId;
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
        if (obj instanceof InboxMessageId) {
            InboxMessageId other = (InboxMessageId) obj;
            return commentId.equals(other.commentId);

        } else {
            return false;
        }
    }

}

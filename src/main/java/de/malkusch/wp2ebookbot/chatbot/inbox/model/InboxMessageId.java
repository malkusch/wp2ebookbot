package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Objects.requireNonNull;

public final class InboxMessageId {

    private final String id;

    public InboxMessageId(String id) {
        if (requireNonNull(id).isEmpty()) {
            throw new IllegalArgumentException("Invalid inbox message id");
        }
        this.id = requireNonNull(id);
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
        if (obj instanceof InboxMessageId) {
            InboxMessageId other = (InboxMessageId) obj;
            return id.equals(other.id);

        } else {
            return false;
        }
    }

}

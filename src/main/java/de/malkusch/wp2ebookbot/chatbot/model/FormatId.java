package de.malkusch.wp2ebookbot.chatbot.model;

import static java.util.Objects.requireNonNull;

public final class FormatId {

    private final String id;

    public FormatId(String id) {
        if (requireNonNull(id).isEmpty()) {
            throw new IllegalArgumentException("Format id must not be empty");
        }
        this.id = id;
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
        if (obj instanceof FormatId) {
            FormatId other = (FormatId) obj;
            return id.equals(other.id);

        } else {
            return false;
        }
    }

}

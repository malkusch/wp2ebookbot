package de.malkusch.wp2ebookbot.chatbot.outbox.model;

import static java.util.Objects.requireNonNull;

public final class WritingPromptId {

    private final String id;

    public WritingPromptId(String id) {
        if (requireNonNull(id.isEmpty())) {
            throw new IllegalArgumentException("Writing prompt id must not be empty");
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
        if (obj instanceof WritingPromptId) {
            WritingPromptId other = (WritingPromptId) obj;
            return id.equals(other.id);

        } else {
            return false;
        }
    }

}

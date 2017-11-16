package de.malkusch.wp2ebookbot.chatbot.outbox.model;

import static java.util.Objects.requireNonNull;

public final class Title {

    private final String title;

    public Title(String title) {
        if (requireNonNull(title).isEmpty()) {
            throw new IllegalArgumentException("Title must not be empty");
        }
        this.title = title;
    }

    public boolean isWritingPrompt() {
        return title.startsWith("[WP]");
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Title) {
            Title other = (Title) obj;
            return title.equals(other.title);

        } else {
            return false;
        }
    }

}

package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Objects.requireNonNull;

public final class Author {

    private final String name;

    public Author(String name) {
        if (requireNonNull(name).isEmpty()) {
            throw new IllegalArgumentException("Author name must not be empty");
        }
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Author) {
            Author other = (Author) obj;
            return name.equals(other.name);

        } else {
            return false;
        }
    }

}

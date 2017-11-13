package de.malkusch.wp2ebookbot.publisher.model;

import static java.util.Objects.requireNonNull;

public final class ThreadTitle {

    private final String title;

    public ThreadTitle(String title) {
        if (requireNonNull(title).isEmpty()) {
            throw new IllegalArgumentException("Thread title must not be empty");
        }
        this.title = title;
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
        if (obj instanceof ThreadTitle) {
            ThreadTitle other = (ThreadTitle) obj;
            return title.equals(other.title);

        } else {
            return false;
        }
    }

}

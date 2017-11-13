package de.malkusch.wp2ebookbot.publisher.model;

import static java.util.Objects.requireNonNull;

import java.net.MalformedURLException;
import java.net.URL;

public final class CommentId {

    private final String id;

    public CommentId(String id) {
        this.id = requireNonNull(id);
        if (!isValid(id)) {
            throw new IllegalArgumentException("Invalid comment id");
        }
    }

    private static boolean isValid(String id) {
        try {
            new URL(id);
            return true;

        } catch (MalformedURLException e) {
            return false;
        }
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
        if (obj instanceof CommentId) {
            CommentId other = (CommentId) obj;
            return id.equals(other.id);

        } else {
            return false;
        }
    }

}

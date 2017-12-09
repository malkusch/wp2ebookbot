package de.malkusch.wp2ebookbot.shared.infrastructure.reddit;

import java.util.Objects;

public final class Self {

    private final String username;

    Self(String username) {
        if (Objects.requireNonNull(username).isEmpty()) {
            throw new IllegalArgumentException("Self must not be empty");
        }
        this.username = username;
    }

    public String username() {
        return username;
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Self) {
            Self other = (Self) obj;
            return username.equals(other.username);

        } else {
            return false;
        }
    }

}

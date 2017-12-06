package de.malkusch.wp2ebookbot.shared.infrastructure.reddit;

import static java.time.Instant.now;
import static java.util.Objects.requireNonNull;

import java.time.Instant;

public final class Authentication {

    private final String token;
    private final String type;
    final Instant expiration;

    Authentication(String type, String token, Instant expiration) {
        this.type = requireNonNull(type);

        if (requireNonNull(token).isEmpty()) {
            throw new IllegalArgumentException("Token must not be empty");
        }
        this.token = token;

        if (requireNonNull(expiration).isBefore(now())) {
            throw new IllegalArgumentException("Expiration must be in the future");
        }
        this.expiration = requireNonNull(expiration);
    }

    @Override
    public String toString() {
        return String.format("%s %s", type, token);
    }

}

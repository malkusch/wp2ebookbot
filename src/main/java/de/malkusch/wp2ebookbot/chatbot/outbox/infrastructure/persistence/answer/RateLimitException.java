package de.malkusch.wp2ebookbot.chatbot.outbox.infrastructure.persistence.answer;

import java.io.IOException;

public final class RateLimitException extends IOException {
    private static final long serialVersionUID = 2592896987717306653L;

    RateLimitException(String message) {
        super(message);
    }

}

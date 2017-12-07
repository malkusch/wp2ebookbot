package de.malkusch.wp2ebookbot.chatbot.outbox.infrastructure.persistence.answer;

import java.io.IOException;

public final class RetryableIOException extends IOException {
    private static final long serialVersionUID = 2592896987717306653L;

    RetryableIOException(Throwable cause) {
        super(cause);
    }

    RetryableIOException(String message) {
        super(message);
    }

}

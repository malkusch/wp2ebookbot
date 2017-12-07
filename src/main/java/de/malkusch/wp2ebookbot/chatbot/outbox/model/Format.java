package de.malkusch.wp2ebookbot.chatbot.outbox.model;

import static java.util.Objects.requireNonNull;

import java.net.URL;

public final class Format {

    final FormatId id;
    final URL url;

    public Format(FormatId id, URL url) {
        this.id = requireNonNull(id);
        this.url = requireNonNull(url);
    }

    public FormatId id() {
        return id;
    }

    public URL url() {
        return url;
    }

}

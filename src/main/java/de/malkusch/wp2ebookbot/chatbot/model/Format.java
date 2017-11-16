package de.malkusch.wp2ebookbot.chatbot.model;

import static java.util.Objects.requireNonNull;

import java.net.URL;

public final class Format {

    final FormatId id;
    final URL url;

    public Format(FormatId id, URL url) {
        this.id = requireNonNull(id);
        this.url = requireNonNull(url);
    }

}

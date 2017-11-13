package de.malkusch.wp2ebookbot.publisher.model;

import static java.util.Objects.requireNonNull;

import java.net.URL;

public final class PublishedFormat {

    final FormatId id;
    final URL url;

    public PublishedFormat(FormatId id, URL url) {
        this.id = requireNonNull(id);
        this.url = requireNonNull(url);
    }

}

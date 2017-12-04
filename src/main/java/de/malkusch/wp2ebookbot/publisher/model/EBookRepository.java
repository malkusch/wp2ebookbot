package de.malkusch.wp2ebookbot.publisher.model;

import java.io.IOException;

public final class EBookRepository {

    private final PublishedFormatRepository publishedFormats;

    EBookRepository(PublishedFormatRepository publishedFormats) {
        this.publishedFormats = publishedFormats;
    }

    public void unpublish(CommentId id) throws IOException {
        publishedFormats.unpublish(id);
    }

}

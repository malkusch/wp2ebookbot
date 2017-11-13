package de.malkusch.wp2ebookbot.publisher.model;

import static java.util.Objects.requireNonNull;

public final class EBook {

    final PublishedFormat[] formats;
    final CommentId commentId;

    EBook(CommentId commentId, PublishedFormat[] formats) {
        this.commentId = requireNonNull(commentId);

        if (requireNonNull(formats).length == 0) {
            throw new IllegalArgumentException("E-Book needs at least one format");
        }
        this.formats = formats;
    }

}

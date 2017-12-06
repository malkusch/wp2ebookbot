package de.malkusch.wp2ebookbot.publisher.model;

import java.util.Arrays;

import de.malkusch.wp2ebookbot.shared.infrastructure.event.Event;

public final class EBookPublished implements Event {

    public final String articleId;
    public final String commentId;
    public final String permissionId;
    public final Format[] formats;

    static public class Format {
        public final String id;
        public final String url;

        private Format(PublishedFormat format) {
            id = format.id.toString();
            url = format.url.toString();
        }
    }

    EBookPublished(EBook book) {
        this.articleId = book.commentId.articleId().toString();
        this.commentId = book.commentId.toString();
        this.permissionId = book.permissionId.toString();
        this.formats = Arrays.stream(book.formats).map(Format::new).toArray(Format[]::new);
    }

}

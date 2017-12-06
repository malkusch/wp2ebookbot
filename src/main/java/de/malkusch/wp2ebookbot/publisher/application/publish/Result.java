package de.malkusch.wp2ebookbot.publisher.application.publish;

import java.util.Arrays;

import de.malkusch.wp2ebookbot.publisher.model.EBook;
import de.malkusch.wp2ebookbot.publisher.model.PublishedFormat;

public final class Result {

    public final Format[] formats;

    public static final class Format {

        public final String url;

        private Format(PublishedFormat format) {
            this.url = format.url().toString();
        }

    }

    Result(EBook ebook) {
        formats = Arrays.stream(ebook.formats()).map(Format::new).toArray(Format[]::new);
    }

}

package de.malkusch.wp2ebookbot.chatbot.outbox.model;

import static java.util.Objects.requireNonNull;

public final class EBook {

    final Format[] formats;
    final Permission permission;

    public EBook(Permission permission, Format[] formats) {
        this.permission = requireNonNull(permission);

        if (requireNonNull(formats).length == 0) {
            throw new IllegalArgumentException("E-Book needs at least one format");
        }
        this.formats = formats;
    }

}

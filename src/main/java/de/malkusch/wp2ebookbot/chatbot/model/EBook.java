package de.malkusch.wp2ebookbot.chatbot.model;

import static java.util.Objects.requireNonNull;

public final class EBook {

    final Format[] formats;
    final PermissionId permissionId;

    public EBook(PermissionId permissionId, Format[] formats) {
        this.permissionId = requireNonNull(permissionId);

        if (requireNonNull(formats).length == 0) {
            throw new IllegalArgumentException("E-Book needs at least one format");
        }
        this.formats = formats;
    }

}

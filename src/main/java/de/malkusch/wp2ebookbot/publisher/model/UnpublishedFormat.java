package de.malkusch.wp2ebookbot.publisher.model;

import static java.util.Objects.requireNonNull;

import java.io.File;

public final class UnpublishedFormat {

    final FormatId id;
    final File file;

    public UnpublishedFormat(FormatId id, File file) {
        this.id = requireNonNull(id);
        this.file = requireNonNull(file);
    }

}

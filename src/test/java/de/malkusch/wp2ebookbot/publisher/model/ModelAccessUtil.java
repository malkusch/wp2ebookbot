package de.malkusch.wp2ebookbot.publisher.model;

import java.io.File;

public final class ModelAccessUtil {

    public static File getFile(UnpublishedFormat unpublishedFormat) {
        return unpublishedFormat.file;
    }

}

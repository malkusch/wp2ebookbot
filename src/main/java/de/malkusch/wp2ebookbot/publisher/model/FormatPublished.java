package de.malkusch.wp2ebookbot.publisher.model;

import de.malkusch.wp2ebookbot.shared.infrastructure.event.Event;

public final class FormatPublished implements Event {

    public final String unpublishedFile;

    FormatPublished(UnpublishedFormat unpublished) {
        this.unpublishedFile = unpublished.file.getPath();
    }

}

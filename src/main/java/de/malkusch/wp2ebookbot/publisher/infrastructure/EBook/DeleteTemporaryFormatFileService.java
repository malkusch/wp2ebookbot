package de.malkusch.wp2ebookbot.publisher.infrastructure.EBook;

import java.io.File;
import java.io.IOException;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import de.malkusch.wp2ebookbot.publisher.model.FormatPublished;

@Service
final class DeleteTemporaryFormatFileService {

    @EventListener(FormatPublished.class)
    void deleteFileAfterPublishing(FormatPublished event) throws IOException {
        if (!new File(event.unpublishedFile).delete()) {
            throw new IOException("Could not delete published file " + event.unpublishedFile);
        }
    }

}

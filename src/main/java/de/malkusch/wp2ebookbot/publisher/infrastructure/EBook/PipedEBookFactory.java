package de.malkusch.wp2ebookbot.publisher.infrastructure.EBook;

import java.io.IOException;

import org.springframework.stereotype.Service;

import de.malkusch.wp2ebookbot.publisher.model.Author;
import de.malkusch.wp2ebookbot.publisher.model.CommentId;
import de.malkusch.wp2ebookbot.publisher.model.EBookFactory;
import de.malkusch.wp2ebookbot.publisher.model.PublishedFormatRepository;
import de.malkusch.wp2ebookbot.publisher.model.ThreadTitle;
import de.malkusch.wp2ebookbot.publisher.model.UnpublishedFormat;
import de.malkusch.wp2ebookbot.shared.infrastructure.event.PublishEventService;

@Service
final class PipedEBookFactory extends EBookFactory {

    private final EPUBFactory epubFactory;
    private final MOBIFactory mobiFactory;

    protected PipedEBookFactory(EPUBFactory epubFactory, MOBIFactory mobiFactory, PublishedFormatRepository publisher,
            PublishEventService eventPublisher) {

        super(publisher, eventPublisher);
        this.epubFactory = epubFactory;
        this.mobiFactory = mobiFactory;
    }

    @Override
    protected UnpublishedFormat[] generateUnpublishedFormats(CommentId id, ThreadTitle title, Author author,
            String comment) throws IOException {

        UnpublishedFormat epub = epubFactory.generateEPUB(id, title, author, comment);
        UnpublishedFormat mobi = mobiFactory.generateMOBI(epub);

        return new UnpublishedFormat[] { epub, mobi };
    }

}

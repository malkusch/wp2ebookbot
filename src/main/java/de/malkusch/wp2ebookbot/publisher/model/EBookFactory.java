package de.malkusch.wp2ebookbot.publisher.model;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import de.malkusch.wp2ebookbot.shared.infrastructure.event.PublishEventService;

public abstract class EBookFactory {

    private final PublishedFormatRepository publisher;
    private final PublishEventService eventPublisher;

    protected EBookFactory(PublishedFormatRepository publisher, PublishEventService eventPublisher) {
        this.publisher = publisher;
        this.eventPublisher = eventPublisher;
    }

    public final EBook publishEBook(Comment comment, PermissionId permissionId) throws IOException {
        requireNonNull(comment);

        UnpublishedFormat[] unpublished = generateUnpublishedFormats(comment.id, comment.title, comment.author,
                comment.body);
        PublishedFormat[] published = publish(unpublished);
        EBook book = new EBook(comment.id, permissionId, published);

        eventPublisher.publish(new EBookPublished(book));
        return book;
    }

    abstract protected UnpublishedFormat[] generateUnpublishedFormats(CommentId id, ThreadTitle title, Author author,
            String body) throws IOException;

    private PublishedFormat[] publish(UnpublishedFormat[] unpublished) throws IOException {
        PublishedFormat[] published = new PublishedFormat[unpublished.length];
        for (int i = 0; i < unpublished.length; i++) {
            published[i] = publisher.publish(unpublished[i]);
            eventPublisher.publish(new FormatPublished(unpublished[i]));
        }
        return published;
    }

}

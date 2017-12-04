package de.malkusch.wp2ebookbot.publisher.model;

import java.io.IOException;

public interface PublishedFormatRepository {

    PublishedFormat publish(UnpublishedFormat unpublished) throws IOException;

    void unpublish(CommentId commentId) throws IOException;

}

package de.malkusch.wp2ebookbot.publisher.model;

import java.io.IOException;

public interface PublishFormatService {

    PublishedFormat publish(UnpublishedFormat unpublished) throws IOException;

}

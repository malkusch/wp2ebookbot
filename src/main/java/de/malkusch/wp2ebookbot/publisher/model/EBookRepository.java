package de.malkusch.wp2ebookbot.publisher.model;

import java.io.IOException;

public interface EBookRepository {

    void unpublish(CommentId commentId) throws IOException;

}

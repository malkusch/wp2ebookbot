package de.malkusch.wp2ebookbot.publisher.application.publish;

import java.io.IOException;

import de.malkusch.wp2ebookbot.publisher.model.Comment;
import de.malkusch.wp2ebookbot.publisher.model.CommentId;
import de.malkusch.wp2ebookbot.publisher.model.CommentRepository;
import de.malkusch.wp2ebookbot.publisher.model.EBookFactory;

public final class PublishCommentAsEBookApplicationService {

    private final EBookFactory ebookFactory;
    private final CommentRepository comments;

    PublishCommentAsEBookApplicationService(EBookFactory ebookFactory, CommentRepository comments) {
        this.ebookFactory = ebookFactory;
        this.comments = comments;
    }

    public void publish(PublishComment command) throws CommentNotFoundException, IOException {
        CommentId commentId = new CommentId(command.commentId);
        Comment comment = comments.findById(commentId).orElseThrow(CommentNotFoundException::new);
        ebookFactory.publishEBook(comment);
    }

}

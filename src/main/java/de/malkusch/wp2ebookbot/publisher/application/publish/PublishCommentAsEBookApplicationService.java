package de.malkusch.wp2ebookbot.publisher.application.publish;

import java.io.IOException;

import de.malkusch.wp2ebookbot.publisher.model.ArticleId;
import de.malkusch.wp2ebookbot.publisher.model.Comment;
import de.malkusch.wp2ebookbot.publisher.model.CommentId;
import de.malkusch.wp2ebookbot.publisher.model.CommentRepository;
import de.malkusch.wp2ebookbot.publisher.model.EBookFactory;
import de.malkusch.wp2ebookbot.publisher.model.PermissionId;

public final class PublishCommentAsEBookApplicationService {

    private final EBookFactory ebookFactory;
    private final CommentRepository comments;

    PublishCommentAsEBookApplicationService(EBookFactory ebookFactory, CommentRepository comments) {
        this.ebookFactory = ebookFactory;
        this.comments = comments;
    }

    public void publish(PublishComment command) throws CommentNotFoundException, IOException {
        ArticleId articleId = new ArticleId(command.articleId);
        CommentId commentId = new CommentId(articleId, command.commentId);
        PermissionId permissionId = new PermissionId(command.permissionId);
        Comment comment = comments.findById(commentId).orElseThrow(CommentNotFoundException::new);
        ebookFactory.publishEBook(comment, permissionId);
    }

}

package de.malkusch.wp2ebookbot.publisher.application.publish;

import java.io.IOException;

import org.springframework.stereotype.Service;

import de.malkusch.wp2ebookbot.publisher.model.ArticleId;
import de.malkusch.wp2ebookbot.publisher.model.Comment;
import de.malkusch.wp2ebookbot.publisher.model.CommentId;
import de.malkusch.wp2ebookbot.publisher.model.CommentRepository;
import de.malkusch.wp2ebookbot.publisher.model.EBook;
import de.malkusch.wp2ebookbot.publisher.model.EBookFactory;
import de.malkusch.wp2ebookbot.publisher.model.PermissionId;

@Service
public final class PublishCommentAsEBookApplicationService {

    private final EBookFactory ebookFactory;
    private final CommentRepository comments;

    PublishCommentAsEBookApplicationService(EBookFactory ebookFactory, CommentRepository comments) {
        this.ebookFactory = ebookFactory;
        this.comments = comments;
    }

    public Result publish(PublishComment command) throws CommentNotFoundException, IOException {
        ArticleId articleId = new ArticleId(command.articleId);
        CommentId commentId = new CommentId(articleId, command.commentId);
        PermissionId permissionId = new PermissionId(command.permissionId);
        Comment comment = comments.findById(commentId).orElseThrow(CommentNotFoundException::new);

        EBook ebook = ebookFactory.publishEBook(comment, permissionId);
        return new Result(ebook);
    }

}

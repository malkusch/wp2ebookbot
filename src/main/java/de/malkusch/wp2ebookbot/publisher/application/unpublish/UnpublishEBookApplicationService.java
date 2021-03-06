package de.malkusch.wp2ebookbot.publisher.application.unpublish;

import java.io.IOException;

import de.malkusch.wp2ebookbot.publisher.model.ArticleId;
import de.malkusch.wp2ebookbot.publisher.model.CommentId;
import de.malkusch.wp2ebookbot.publisher.model.EBookRepository;

public final class UnpublishEBookApplicationService {

    private final EBookRepository books;

    UnpublishEBookApplicationService(EBookRepository books) {
        this.books = books;
    }

    public void unpublishEBook(UnpublishEBook command) throws IOException {
        ArticleId articleId = new ArticleId(command.articleId);
        CommentId commentId = new CommentId(articleId, command.commentId);
        books.unpublish(commentId);
    }

}

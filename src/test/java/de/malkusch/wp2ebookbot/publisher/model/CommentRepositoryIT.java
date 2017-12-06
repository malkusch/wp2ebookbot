package de.malkusch.wp2ebookbot.publisher.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import de.malkusch.wp2ebookbot.test.IntegrationTest;

@IntegrationTest
@RunWith(SpringRunner.class)
public class CommentRepositoryIT {

    @Autowired
    private CommentRepository comments;

    @Test
    public void shouldFindComment() throws IOException {
        ArticleId articleId = new ArticleId("7hsy5b");
        CommentId commentId = new CommentId(articleId, "dqu6y0p");

        Comment comment = comments.findById(commentId).get();

        assertEquals(new ThreadTitle("I Don't Like Your Examples!"), comment.title);
        assertEquals(new Author("recycled_ideas"), comment.author);
    }

    @Test
    public void shouldNotFindWhenCommentofOtherArticle() throws IOException {
        ArticleId articleId = new ArticleId("7hsy5b");
        CommentId commentId = new CommentId(articleId, "dquqsji");

        Optional<Comment> comment = comments.findById(commentId);

        assertFalse(comment.isPresent());
    }

    @Test
    public void shouldNotFindWhenArticleNotExists() throws IOException {
        ArticleId articleId = new ArticleId("nonExisting");
        CommentId commentId = new CommentId(articleId, "dquqsji");

        Optional<Comment> comment = comments.findById(commentId);

        assertFalse(comment.isPresent());
    }

    @Test
    public void shouldNotFindWhenCommentNotExists() throws IOException {
        ArticleId articleId = new ArticleId("7hsy5b");
        CommentId commentId = new CommentId(articleId, "nonExisting");

        Optional<Comment> comment = comments.findById(commentId);

        assertFalse(comment.isPresent());
    }

}

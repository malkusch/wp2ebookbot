package de.malkusch.wp2ebookbot.chatbot.outbox.infrastructure.persistence.answer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import de.malkusch.wp2ebookbot.chatbot.outbox.infrastructure.persistence.answer.RestService.Permalink;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.CommentId;
import de.malkusch.wp2ebookbot.test.IntegrationTest;

@IntegrationTest
@RunWith(SpringRunner.class)
public class RetryServiceIT {

    @Autowired
    private RetryService service;

    @Autowired
    private AnswerableCommentService answerables;

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotPostEmptyAnswer() throws IOException {
        CommentId parent = answerables.findAnswerableCommentId();
        service.answerComment(parent, "");
    }

    @Test(expected = IOException.class)
    public void shouldNotAnswerInvalidComment() throws IOException {
        CommentId parent = new CommentId("invalid");
        service.answerComment(parent, "anything");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAnswerOldComment() throws IOException {
        CommentId parent = new CommentId("dfj5i7g");
        service.answerComment(parent, "anything");
    }

    @Test
    public void shouldAnswerComment() throws IOException {
        CommentId parent = answerables.findAnswerableCommentId();
        Permalink link = service.answerComment(parent, "test");

        assertNotNull(link.link);
        assertFalse(link.link.isEmpty());
    }

}

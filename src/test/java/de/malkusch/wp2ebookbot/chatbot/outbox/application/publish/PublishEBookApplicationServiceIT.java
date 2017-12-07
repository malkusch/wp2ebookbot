package de.malkusch.wp2ebookbot.chatbot.outbox.application.publish;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import de.malkusch.wp2ebookbot.chatbot.outbox.infrastructure.persistence.answer.AnswerableCommentService;
import de.malkusch.wp2ebookbot.test.IntegrationTest;

@IntegrationTest
@RunWith(SpringRunner.class)
public class PublishEBookApplicationServiceIT {

    @Autowired
    private AnswerableCommentService answerables;

    @Autowired
    private PublishEBookApplicationService service;

    @Test
    public void shouldPublish() {
        Publish.Format epub = new Publish.Format();
        epub.id = "EPUB";
        epub.url = "http://example.org/epub";
        Publish.Format mobi = new Publish.Format();
        mobi.id = "MOBI";
        mobi.url = "http://example.org/mobi";
        Publish command = new Publish();
        command.permissionId = answerables.findAnswerableCommentId().toString();
        command.formats = new Publish.Format[] { epub, mobi };

        service.publish(command);
    }

}

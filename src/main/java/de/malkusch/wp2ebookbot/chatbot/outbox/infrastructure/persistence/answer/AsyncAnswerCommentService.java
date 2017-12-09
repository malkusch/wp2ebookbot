package de.malkusch.wp2ebookbot.chatbot.outbox.infrastructure.persistence.answer;

import static de.malkusch.wp2ebookbot.chatbot.outbox.infrastructure.persistence.answer.AnswerCommentServiceConfiguration.ANSWER_EXECUTOR;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import de.malkusch.wp2ebookbot.chatbot.outbox.model.AnswerCommentService;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.CommentId;

@Service
final class AsyncAnswerCommentService implements AnswerCommentService {

    private final RetryService service;
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncAnswerCommentService.class);

    AsyncAnswerCommentService(RetryService service) {
        this.service = service;
    }

    @Async(ANSWER_EXECUTOR)
    @Override
    public void answerComment(CommentId parent, String response) {
        try {
            LOGGER.info("Enqueing answer of {}", parent);
            service.answerComment(parent, response);

        } catch (IOException e) {
            LOGGER.error("Failed to answer " + parent, e);
        }
    }

}

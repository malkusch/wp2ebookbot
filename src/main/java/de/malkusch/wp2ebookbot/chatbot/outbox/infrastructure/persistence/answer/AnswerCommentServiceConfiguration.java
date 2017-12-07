package de.malkusch.wp2ebookbot.chatbot.outbox.infrastructure.persistence.answer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AnswerCommentServiceConfiguration {

    static final String ANSWER_EXECUTOR = "AsyncAnswerCommentService";

    @Bean(ANSWER_EXECUTOR)
    Executor answerExecutor() {
        return Executors.newSingleThreadExecutor();
    }

}

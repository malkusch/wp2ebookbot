package de.malkusch.wp2ebookbot.chatbot.outbox.infrastructure.persistence.answer;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import de.malkusch.wp2ebookbot.chatbot.outbox.infrastructure.persistence.answer.RestService.Permalink;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.CommentId;

@Service
final class RetryService {

    private final RestService rest;
    private final RetryTemplate retry;

    RetryService(RestService rest, @Value("${reddit.comment.initialBackOffSeconds}") int initialBackOffSeconds,
            @Value("${reddit.comment.maxBackOffSeconds}") int maxBackOffSeconds,
            @Value("${reddit.comment.retries}") int retries) {

        this.rest = rest;

        retry = new RetryTemplate();

        ExponentialBackOffPolicy backOff = new ExponentialBackOffPolicy();
        backOff.setInitialInterval(initialBackOffSeconds * 1000);
        backOff.setMaxInterval(maxBackOffSeconds * 1000);
        retry.setBackOffPolicy(backOff);

        SimpleRetryPolicy policy = new SimpleRetryPolicy(retries,
                Collections.singletonMap(RetryableIOException.class, true));
        retry.setRetryPolicy(policy);
    }

    Permalink answerComment(CommentId parent, String response) throws IOException {
        return retry.execute(c -> rest.answerComment(parent, response));
    }

}

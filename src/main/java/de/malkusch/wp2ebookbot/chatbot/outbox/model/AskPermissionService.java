package de.malkusch.wp2ebookbot.chatbot.outbox.model;

import static java.util.Arrays.stream;

import java.io.IOException;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AskPermissionService {

    private final WritingPromptRepository writingPrompts;
    private final WritingPrompSpecification wpSpec;
    private final AnswerCommentService reddit;
    final String question;
    private volatile Instant lastSince;
    private static Logger LOGGER = LoggerFactory.getLogger(AskPermissionService.class);

    public AskPermissionService(WritingPromptRepository writingPrompts, WritingPrompSpecification wpSpec,
            String question, AnswerCommentService reddit) {

        this.writingPrompts = writingPrompts;
        this.wpSpec = wpSpec;
        this.question = question;
        this.reddit = reddit;
        this.lastSince = Instant.now();
    }

    public void askNewTopCommentsAuthorForPermission() throws IOException {
        WritingPrompt[] newWPs = writingPrompts.findEligibleWritingPromptsSince(lastSince, wpSpec);
        LOGGER.info("Found {} new WritingPrompts since {}", newWPs.length, lastSince);
        lastSince = Instant.now();

        stream(newWPs).filter(wpSpec::IsSatisfiedBy).forEach(this::ask);
    }

    private void ask(WritingPrompt wp) {
        LOGGER.info("Asking {} for permission", wp);
        reddit.answerComment(wp.topComment.id, question);
    }

}

package de.malkusch.wp2ebookbot.chatbot.outbox.model;

import static java.util.Arrays.stream;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AskPermissionService {

    private final WritingPromptRepository writingPrompts;
    private final WritingPrompSpecification wpSpec;
    private final AnswerCommentService reddit;
    final String question;
    private volatile TimeWindow lastTimeWindow;
    private static Logger LOGGER = LoggerFactory.getLogger(AskPermissionService.class);

    public AskPermissionService(WritingPromptRepository writingPrompts, WritingPrompSpecification wpSpec,
            String question, AnswerCommentService reddit) {

        this.writingPrompts = writingPrompts;
        this.wpSpec = wpSpec;
        this.question = question;
        this.reddit = reddit;
        this.lastTimeWindow = new TimeWindow(Instant.now(), Instant.now());
    }

    public void askNewTopCommentsAuthorForPermission() throws IOException {
        Optional<TimeWindow> window = nextWindow();
        if (!window.isPresent()) {
            return;
        }

        WritingPrompt[] newWPs = writingPrompts.findEligibleWritingPromptsSince(window.get(), wpSpec);
        LOGGER.info("Found {} new WritingPrompts in {}", newWPs.length, window.get());
        lastTimeWindow = window.get();

        stream(newWPs).filter(wpSpec::IsSatisfiedBy).forEach(this::ask);
    }

    private Optional<TimeWindow> nextWindow() {
        Instant until = Instant.now().minus(wpSpec.minWritingPromptAge);
        if (until.isAfter(Instant.now())) {
            return Optional.empty();
        }
        if (!until.isAfter(lastTimeWindow.exclusiveUntil)) {
            return Optional.empty();
        }
        return Optional.of(lastTimeWindow.adjacentUntilExclusively(until));

    }

    private void ask(WritingPrompt wp) {
        LOGGER.info("Asking {} for permission", wp);
        reddit.answerComment(wp.topComment.id, question);
    }

}

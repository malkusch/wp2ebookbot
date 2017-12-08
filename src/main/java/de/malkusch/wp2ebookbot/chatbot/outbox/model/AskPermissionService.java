package de.malkusch.wp2ebookbot.chatbot.outbox.model;

import static java.util.Arrays.stream;

import java.io.IOException;
import java.time.Instant;;

public final class AskPermissionService {

    private final WritingPromptRepository writingPrompts;
    private final WritingPrompSpecification wpSpec;
    private final AnswerCommentService reddit;
    final String question;
    private volatile Instant lastSince;

    AskPermissionService(WritingPromptRepository writingPrompts, WritingPrompSpecification wpSpec, String question,
            AnswerCommentService reddit) {

        this.writingPrompts = writingPrompts;
        this.wpSpec = wpSpec;
        this.question = question;
        this.reddit = reddit;
        this.lastSince = Instant.now();
    }

    public void askNewTopCommentsAuthorForPermission() throws IOException {
        WritingPrompt[] newWPs = writingPrompts.findEligibleWritingPromptsSince(lastSince, wpSpec);
        lastSince = Instant.now();
        stream(newWPs).filter(wpSpec::IsSatisfiedBy).forEach(wp -> reddit.answerComment(wp.topComment.id, question));
    }

}

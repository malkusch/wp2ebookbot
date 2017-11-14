package de.malkusch.wp2ebookbot.chatbot.model;

import static java.util.Arrays.stream;

import java.io.IOException;
import java.time.Instant;;

public final class AskNewWritingPromptsService {

    private final WritingPromptRepository writingPrompts;
    private final WritingPrompSpecification wpSpec;
    private final RedditAPI reddit;
    private final String question;
    private Instant lastSince;

    protected AskNewWritingPromptsService(WritingPromptRepository writingPrompts, WritingPrompSpecification wpSpec,
            String question, RedditAPI reddit) {

        this.writingPrompts = writingPrompts;
        this.wpSpec = wpSpec;
        this.question = question;
        this.reddit = reddit;
        this.lastSince = Instant.now();
    }

    public void askNewWritingPromptsAuthorsForPermission() throws IOException {
        WritingPrompt[] newWPs = writingPrompts.findEligibleWritingPromptsSince(lastSince, wpSpec);
        lastSince = Instant.now();
        stream(newWPs).filter(wpSpec::IsSatisfiedBy).forEach(wp -> reddit.answerComment(wp.topComment.id, question));
    }

}

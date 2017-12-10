package de.malkusch.wp2ebookbot.chatbot.outbox.model;

import java.io.IOException;
import java.time.Instant;

public abstract class WritingPromptRepository {

    public abstract WritingPrompt[] findEligibleWritingPromptsSince(TimeWindow window, WritingPrompSpecification hint)
            throws IOException;

    protected final Comment hydrateComment(CommentId id, Votes votes, Words words) {
        return new Comment(id, votes, words);
    }

    protected final WritingPrompt hydrateWritingPrompt(WritingPromptId id, Instant created, Title title, Votes votes,
            Comment topComment) {

        return new WritingPrompt(id, created, title, votes, topComment);
    }

}

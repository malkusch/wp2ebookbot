package de.malkusch.wp2ebookbot.chatbot.model;

import java.io.IOException;
import java.time.Instant;

public abstract class WritingPromptRepository {

    public abstract WritingPrompt[] findEligibleWritingPromptsSince(Instant since, WritingPrompSpecification wpSpec)
            throws IOException;

    protected final Comment hydrateComment(CommentId id, Author author, Votes votes, Words words) {
        return new Comment(id, author, votes, words);
    }

    protected final WritingPrompt hydrateWritingPrompt(WritingPromptId id, Votes votes, Comment topComment) {
        return new WritingPrompt(id, votes, topComment);
    }

}

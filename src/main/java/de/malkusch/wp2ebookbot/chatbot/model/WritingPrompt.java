package de.malkusch.wp2ebookbot.chatbot.model;

import static java.util.Objects.requireNonNull;

public final class WritingPrompt {

    final Votes votes;
    final WritingPromptId id;
    final Comment topComment;

    WritingPrompt(WritingPromptId id, Votes votes, Comment topComment) {
        this.id = requireNonNull(id);
        this.votes = requireNonNull(votes);
        this.topComment = requireNonNull(topComment);
    }

}

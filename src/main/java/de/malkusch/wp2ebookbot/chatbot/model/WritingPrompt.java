package de.malkusch.wp2ebookbot.chatbot.model;

import static java.util.Objects.requireNonNull;

public final class WritingPrompt {

    final Votes votes;
    final WritingPromptId id;
    final Comment topComment;
    final Title title;

    WritingPrompt(WritingPromptId id, Title title, Votes votes, Comment topComment) {
        this.id = requireNonNull(id);
        this.votes = requireNonNull(votes);
        this.topComment = requireNonNull(topComment);

        if (!requireNonNull(title).isWritingPrompt()) {
            throw new IllegalArgumentException("Title is no writing prompt");
        }
        this.title = requireNonNull(title);
    }

}

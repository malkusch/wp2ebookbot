package de.malkusch.wp2ebookbot.chatbot.outbox.model;

import static java.util.Objects.requireNonNull;

import java.time.Instant;

public final class WritingPrompt {

    final Votes votes;
    final Instant created;
    final WritingPromptId id;
    final Comment topComment;
    final Title title;

    WritingPrompt(WritingPromptId id, Instant created, Title title, Votes votes, Comment topComment) {
        this.id = requireNonNull(id);
        this.created = requireNonNull(created);
        this.votes = requireNonNull(votes);
        this.topComment = requireNonNull(topComment);

        if (!requireNonNull(title).isWritingPrompt()) {
            throw new IllegalArgumentException("Title is no writing prompt");
        }
        this.title = requireNonNull(title);
    }

    @Override
    public String toString() {
        return topComment.toString();
    }

}

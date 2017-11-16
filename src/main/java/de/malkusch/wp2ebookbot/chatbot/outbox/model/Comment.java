package de.malkusch.wp2ebookbot.chatbot.outbox.model;

import static java.util.Objects.requireNonNull;

public final class Comment {

    final Votes votes;
    final CommentId id;
    final Words words;

    Comment(CommentId id, Votes votes, Words words) {
        this.id = requireNonNull(id);
        this.votes = requireNonNull(votes);
        this.words = requireNonNull(words);
    }

    @Override
    public String toString() {
        return id.toString();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Comment) {
            Comment other = (Comment) obj;
            return id.equals(other.id);

        } else {
            return false;
        }
    }

}

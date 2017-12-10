package de.malkusch.wp2ebookbot.chatbot.outbox.model;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.Instant;

public final class WritingPrompSpecification {

    private final Votes minWritingPromptVotes;
    final Duration minWritingPromptAge;
    private final Words minCommentWords;
    private final Votes minCommentVotes;

    public WritingPrompSpecification(Votes minWritingPromptVotes, Duration minWritingPromptAge, Words minCommentWords,
            Votes minCommentVotes) {

        this.minWritingPromptVotes = requireNonNull(minWritingPromptVotes);
        this.minWritingPromptAge = requireNonNull(minWritingPromptAge);
        this.minCommentWords = requireNonNull(minCommentWords);
        this.minCommentVotes = requireNonNull(minCommentVotes);
    }

    public boolean IsSatisfiedBy(WritingPrompt writingPrompt) {
        requireNonNull(writingPrompt);
        return isSatisfiedByWritingPromptVotes(writingPrompt.votes)
                && isSatisfiedByWritingPromptAge(writingPrompt.created) && isSatisfiedBy(writingPrompt.topComment);
    }

    private boolean isSatisfiedByWritingPromptAge(Instant created) {
        Duration age = Duration.between(created, Instant.now());
        return age.compareTo(minWritingPromptAge) >= 0;
    }

    public boolean isSatisfiedByWritingPromptVotes(Votes votes) {
        requireNonNull(votes);
        return votes.count >= minWritingPromptVotes.count;
    }

    public boolean isSatisfiedBy(Comment comment) {
        requireNonNull(comment);
        return comment.words.count >= minCommentWords.count && comment.votes.count >= minCommentVotes.count;
    }

}

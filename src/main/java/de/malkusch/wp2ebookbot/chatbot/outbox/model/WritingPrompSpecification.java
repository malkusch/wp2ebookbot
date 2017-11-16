package de.malkusch.wp2ebookbot.chatbot.outbox.model;

import static java.util.Objects.requireNonNull;

public final class WritingPrompSpecification {

    private final Votes minWritingPromptVotes;
    private final Words minCommentWords;
    private final Votes minCommentVotes;

    public WritingPrompSpecification(Votes minWritingPromptVotes, Words minCommentWords, Votes minCommentVotes) {
        this.minWritingPromptVotes = requireNonNull(minWritingPromptVotes);
        this.minCommentWords = requireNonNull(minCommentWords);
        this.minCommentVotes = requireNonNull(minCommentVotes);
    }

    public boolean IsSatisfiedBy(WritingPrompt writingPrompt) {
        requireNonNull(writingPrompt);
        return writingPrompt.votes.count >= minWritingPromptVotes.count
                && writingPrompt.topComment.words.count >= minCommentWords.count
                && writingPrompt.topComment.votes.count >= minCommentVotes.count;
    }

}

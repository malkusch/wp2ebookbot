package de.malkusch.wp2ebookbot.chatbot.outbox.model;

import static java.util.Objects.requireNonNull;

public abstract class PublishEBookService {

    private final AnswerCommentService reddit;

    protected PublishEBookService(AnswerCommentService reddit) {
        this.reddit = reddit;
    }

    public final void publish(EBook book) {
        String message = publishMessage(requireNonNull(book.formats));
        reddit.answerComment(book.permission.commentId, message);
    }

    protected abstract String publishMessage(Format[] formats);

}

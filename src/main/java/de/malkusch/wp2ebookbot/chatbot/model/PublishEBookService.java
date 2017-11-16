package de.malkusch.wp2ebookbot.chatbot.model;

import static java.util.Objects.requireNonNull;

public abstract class PublishEBookService {

    private final RedditAPI reddit;

    PublishEBookService(RedditAPI reddit) {
        this.reddit = reddit;
    }

    public final void publish(EBook book) {
        String message = publishMessage(requireNonNull(book.formats));
        reddit.answerComment(book.permissionId.commentId, message);
    }

    protected abstract String publishMessage(Format[] formats);

}

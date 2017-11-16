package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

public final class InboxMessage {

    final InboxMessageId id;
    final CommentId commentId;
    final Optional<InboxMessageContext> context;
    final Author author;
    final String message;
    final Title title;

    InboxMessage(InboxMessageId id, CommentId commentId, Author author, Title title, String message,
            Optional<InboxMessageContext> context) {

        this.id = requireNonNull(id);
        this.commentId = requireNonNull(commentId);
        this.author = requireNonNull(author);
        this.title = requireNonNull(title);
        this.message = requireNonNull(message);
        this.context = requireNonNull(context);
    }

    public InboxMessageId id() {
        return id;
    }

}

package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

public final class InboxMessageContext {

    final Optional<InboxMessageContext> context;
    final Author author;
    final String message;
    final CommentId commentId;

    InboxMessageContext(Author author, CommentId commentId, String message, Optional<InboxMessageContext> context) {
        this.author = requireNonNull(author);
        this.commentId = requireNonNull(commentId);
        this.message = requireNonNull(message);
        this.context = requireNonNull(context);
    }

}

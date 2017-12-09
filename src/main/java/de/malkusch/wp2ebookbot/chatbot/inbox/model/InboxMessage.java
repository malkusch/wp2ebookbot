package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

public final class InboxMessage {

    final InboxMessageId id;
    final Optional<InboxMessageContext> context;
    final Author author;
    final String message;
    final Title title;

    InboxMessage(InboxMessageId id, Author author, Title title, String message, Optional<InboxMessageContext> context) {
        this.id = requireNonNull(id);
        this.author = requireNonNull(author);
        this.title = requireNonNull(title);
        this.message = requireNonNull(message);
        this.context = requireNonNull(context);
    }

    public InboxMessageId id() {
        return id;
    }

    Optional<InboxMessageContext> topCommentContext() {
        return context.map(InboxMessageContext::topCommentContext);
    }

    boolean isFromTopCommentAuthor() {
        return topCommentContext().map(c -> c.author).filter(author::equals).isPresent();
    }

}

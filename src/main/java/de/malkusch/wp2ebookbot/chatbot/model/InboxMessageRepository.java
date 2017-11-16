package de.malkusch.wp2ebookbot.chatbot.model;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public abstract class InboxMessageRepository {

    public abstract Collection<InboxMessage> fetchNewMessages() throws IOException;

    public abstract void markRead(InboxMessageId id) throws IOException;

    protected final InboxMessage hydrateInboxMessage(InboxMessageId id, CommentId commentId, Author author, Title title,
            String message, Optional<InboxMessageContext> context) {

        return new InboxMessage(id, commentId, author, title, message, context);
    }

    protected final InboxMessageContext hydrateContext(Author author, CommentId commentId, String message,
            Optional<InboxMessageContext> context) {

        return new InboxMessageContext(author, commentId, message, context);
    }

}

package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public abstract class InboxMessageRepository {

    public abstract Collection<InboxMessage> fetchNewMessages() throws IOException;

    public abstract void markRead(InboxMessageId id) throws IOException;

    protected static InboxMessage hydrateInboxMessage(InboxMessageId id, Author author, Title title, String message,
            Optional<InboxMessageContext> context) {

        return new InboxMessage(id, author, title, message, context);
    }

    protected static InboxMessageContext hydrateContext(Author author, CommentId commentId, String message,
            Optional<InboxMessageContext> context) {

        return new InboxMessageContext(author, commentId, message, context);
    }

}

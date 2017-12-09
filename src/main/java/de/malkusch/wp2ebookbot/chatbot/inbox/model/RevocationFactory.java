package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Optional;

public final class RevocationFactory {

    private final Author self;

    RevocationFactory(Author self) {
        this.self = self;
    }

    public Collection<Revocation> fromInbox(Collection<InboxMessage> messages) {
        return requireNonNull(messages).stream().filter(this::isRevocation).map(this::convert).collect(toList());
    }

    private Revocation convert(InboxMessage message) {
        CommentId topCommentId = message.topCommentContext().map(c -> c.commentId).get();
        RevocationId id = new RevocationId(message.id.commentId);
        return new Revocation(id, topCommentId, message.message);
    }

    private boolean isRevocation(InboxMessage message) {
        return message.title.isWritingPrompt() && containsEBookPublication(message) && message.isFromTopCommentAuthor();
    }

    private boolean containsEBookPublication(InboxMessage message) {
        return publicationContext(message).filter(c -> c.author.equals(self)).isPresent();
    }

    private static Optional<InboxMessageContext> publicationContext(InboxMessage message) {
        return message.context;
    }

}

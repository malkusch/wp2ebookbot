package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Optional;

public final class PermissionFactory {

    private final PermissionQuestion question;
    final Author self;

    public PermissionFactory(PermissionQuestion question, Author self) {
        this.question = question;
        this.self = self;
    }

    public Collection<Permission> fromInbox(Collection<InboxMessage> messages) {
        return requireNonNull(messages).stream().filter(this::isPermissionAnswer).map(this::convert).collect(toList());
    }

    private boolean isPermissionAnswer(InboxMessage message) {
        return message.title.isWritingPrompt() && containsPermissionQuestion(message)
                && message.isFromTopCommentAuthor();
    }

    private Permission convert(InboxMessage message) {
        CommentId topCommentId = message.topCommentContext().map(c -> c.commentId).get();
        PermissionId permissionId = new PermissionId(message.id.commentId);
        return new Permission(permissionId, topCommentId, message.message);
    }

    private boolean containsPermissionQuestion(InboxMessage message) {
        return questionContext(message)
                .filter(c -> c.author.equals(self) && new PermissionQuestion(c.message).equals(question)).isPresent();
    }

    private static Optional<InboxMessageContext> questionContext(InboxMessage message) {
        return message.context;
    }

}

package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Optional;

public final class PermissionFactory {

    private final PermissionQuestion question;
    private final Author self;

    PermissionFactory(PermissionQuestion question, Author self) {
        this.question = question;
        this.self = self;
    }

    public Collection<Permission> fromInbox(Collection<InboxMessage> messages) {
        return requireNonNull(messages).stream().filter(this::isPermissionAnswer).map(this::convert).collect(toList());
    }

    private boolean isPermissionAnswer(InboxMessage message) {
        return message.title.isWritingPrompt() && containsPermissionQuestion(message)
                && isFromTopCommentAuthor(message);
    }

    private Permission convert(InboxMessage message) {
        CommentId topCommentId = topCommentContext(message).map(c -> c.commentId).get();
        PermissionId permissionId = new PermissionId(message.commentId);
        return new Permission(permissionId, topCommentId, message.message);
    }

    private boolean containsPermissionQuestion(InboxMessage message) {
        return questionContext(message)
                .filter(c -> c.author.equals(self) && new PermissionQuestion(c.message).equals(question)).isPresent();
    }

    private boolean isFromTopCommentAuthor(InboxMessage message) {
        return topCommentContext(message).map(c -> c.author).filter(message.author::equals).isPresent();
    }

    private static Optional<InboxMessageContext> questionContext(InboxMessage message) {
        return message.context;
    }

    private static Optional<InboxMessageContext> topCommentContext(InboxMessage message) {
        return message.context.flatMap(c -> c.context);
    }

}

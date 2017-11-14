package de.malkusch.wp2ebookbot.chatbot.model;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Optional;

public final class PermissionAnswerFactory {

    private final RedditAPI reddit;
    private final AskPermissionService askService;

    PermissionAnswerFactory(RedditAPI reddit, AskPermissionService askService) {
        this.reddit = reddit;
        this.askService = askService;
    }

    public Collection<PermissionAnswer> fromInbox(Collection<InboxMessage> messages) {
        return requireNonNull(messages).stream().filter(this::isPermissionAnswer).map(this::convert).collect(toList());
    }

    private boolean isPermissionAnswer(InboxMessage message) {
        return message.title.isWritingPrompt() && containsPermissionQuestion(message)
                && isFromTopCommentAuthor(message);
    }

    private PermissionAnswer convert(InboxMessage message) {
        CommentId topCommentId = topCommentContext(message).map(c -> c.commentId).get();
        return new PermissionAnswer(topCommentId, message.message);
    }

    private boolean containsPermissionQuestion(InboxMessage message) {
        return questionContext(message)
                .filter(c -> c.author.equals(reddit.self()) && c.message.equals(askService.question)).isPresent();
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

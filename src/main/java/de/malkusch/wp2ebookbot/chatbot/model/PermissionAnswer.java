package de.malkusch.wp2ebookbot.chatbot.model;

import static java.util.Objects.requireNonNull;

public final class PermissionAnswer {

    final CommentId topCommentId;
    final String answer;

    PermissionAnswer(CommentId topCommentId, String answer) {
        this.topCommentId = requireNonNull(topCommentId);
        this.answer = requireNonNull(answer);
    }

}

package de.malkusch.wp2ebookbot.chatbot.model;

import static java.util.Objects.requireNonNull;

public final class PermissionAnswer {

    final CommentId topCommentId;
    final String answer;

    PermissionAnswer(CommentId topCommentId, String answer) {
        this.topCommentId = requireNonNull(topCommentId);
        this.answer = requireNonNull(answer);
    }

    @Override
    public int hashCode() {
        return topCommentId.hashCode() + answer.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PermissionAnswer) {
            PermissionAnswer other = (PermissionAnswer) obj;
            return topCommentId.equals(other.topCommentId) && answer.equals(other.answer);

        } else {
            return false;
        }
    }

}

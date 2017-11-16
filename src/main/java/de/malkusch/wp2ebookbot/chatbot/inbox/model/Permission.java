package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Objects.requireNonNull;

public final class Permission {

    final PermissionId permissionId;
    final CommentId topCommentId;
    final String answer;

    Permission(PermissionId permissionId, CommentId topCommentId, String answer) {
        this.permissionId = requireNonNull(permissionId);
        this.topCommentId = requireNonNull(topCommentId);
        this.answer = requireNonNull(answer);
    }

    @Override
    public int hashCode() {
        return permissionId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Permission) {
            Permission other = (Permission) obj;
            return permissionId.equals(other.permissionId);

        } else {
            return false;
        }
    }

}

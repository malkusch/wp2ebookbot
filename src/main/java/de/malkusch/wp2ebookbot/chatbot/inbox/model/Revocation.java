package de.malkusch.wp2ebookbot.chatbot.inbox.model;

public final class Revocation {

    final RevocationId id;
    final CommentId topCommentId;
    final String revocation;

    Revocation(RevocationId id, CommentId topCommentId, String revocation) {
        this.id = id;
        this.topCommentId = topCommentId;
        this.revocation = revocation;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Revocation) {
            Revocation other = (Revocation) obj;
            return id.equals(other.id);

        } else {
            return false;
        }
    }

}

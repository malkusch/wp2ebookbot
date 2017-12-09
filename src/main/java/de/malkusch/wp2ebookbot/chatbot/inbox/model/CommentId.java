package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Objects.requireNonNull;

public final class CommentId {

    private final String id;
    final ArticleId articleId;

    public CommentId(ArticleId articleId, String id) {
        this.articleId = requireNonNull(articleId);

        if (requireNonNull(id.isEmpty())) {
            throw new IllegalArgumentException("Comment id must not be empty");
        }
        this.id = id;
    }

    public ArticleId articleId() {
        return articleId;
    }

    public String fullname() {
        return "t1_" + id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int hashCode() {
        return articleId.hashCode() + id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CommentId) {
            CommentId other = (CommentId) obj;
            return id.equals(other.id) && articleId.equals(other.articleId);

        } else {
            return false;
        }
    }

}

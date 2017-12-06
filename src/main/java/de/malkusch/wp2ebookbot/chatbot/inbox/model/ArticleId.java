package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Objects.requireNonNull;

public final class ArticleId {

    private final String id;

    public ArticleId(String id) {
        if (requireNonNull(id.isEmpty())) {
            throw new IllegalArgumentException("Article id must not be empty");
        }
        this.id = id;
    }

    public String fullname() {
        return "t3_" + id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ArticleId) {
            ArticleId other = (ArticleId) obj;
            return id.equals(other.id);

        } else {
            return false;
        }
    }

}

package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Objects.requireNonNull;

public final class PermissionQuestion {

    private final String question;

    public PermissionQuestion(String name) {
        if (requireNonNull(name).isEmpty()) {
            throw new IllegalArgumentException("Question must not be empty");
        }
        this.question = name;
    }

    @Override
    public String toString() {
        return question;
    }

    @Override
    public int hashCode() {
        return question.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PermissionQuestion) {
            PermissionQuestion other = (PermissionQuestion) obj;
            return question.equals(other.question);

        } else {
            return false;
        }
    }

}

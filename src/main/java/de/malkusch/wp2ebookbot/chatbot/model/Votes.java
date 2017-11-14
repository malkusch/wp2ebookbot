package de.malkusch.wp2ebookbot.chatbot.model;

public final class Votes {

    final int count;

    public Votes(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return Integer.toString(count);
    }

    @Override
    public int hashCode() {
        return count;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Votes) {
            Votes other = (Votes) obj;
            return count == other.count;

        } else {
            return false;
        }
    }

}

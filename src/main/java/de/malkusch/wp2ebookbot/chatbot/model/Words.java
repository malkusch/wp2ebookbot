package de.malkusch.wp2ebookbot.chatbot.model;

public final class Words {

    final int count;

    public Words(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Words must not be negative");
        }
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
        if (obj instanceof Words) {
            Words other = (Words) obj;
            return count == other.count;

        } else {
            return false;
        }
    }

}

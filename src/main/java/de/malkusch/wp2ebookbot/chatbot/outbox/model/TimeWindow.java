package de.malkusch.wp2ebookbot.chatbot.outbox.model;

import static java.util.Objects.requireNonNull;

import java.time.Instant;

public final class TimeWindow {

    private final Instant inclusiveFrom;
    final Instant exclusiveUntil;

    public TimeWindow(Instant inclusiveFrom, Instant exclusiveUntil) {
        this.inclusiveFrom = requireNonNull(inclusiveFrom);
        this.exclusiveUntil = requireNonNull(exclusiveUntil);
    }

    public boolean includes(Instant time) {
        requireNonNull(time);
        return time.equals(inclusiveFrom) || (inclusiveFrom.isBefore(time) && exclusiveUntil.isAfter(time));
    }

    public TimeWindow adjacentUntilExclusively(Instant exclusiveUntil) {
        if (!requireNonNull(exclusiveUntil).isAfter(this.exclusiveUntil)) {
            throw new IllegalArgumentException("Adjacent until must be after this until");
        }
        return new TimeWindow(this.exclusiveUntil, exclusiveUntil);
    }

    @Override
    public String toString() {
        return String.format("[%s, %s)", inclusiveFrom, exclusiveUntil);
    }

    @Override
    public int hashCode() {
        return inclusiveFrom.hashCode() + exclusiveUntil.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TimeWindow) {
            TimeWindow other = (TimeWindow) obj;
            return inclusiveFrom.equals(other.inclusiveFrom) && exclusiveUntil.equals(other.exclusiveUntil);

        } else {
            return false;
        }
    }

}

package de.malkusch.wp2ebookbot.publisher.model;

import static java.util.Objects.requireNonNull;

public final class PermissionId {

    private final String id;

    public PermissionId(String id) {
        this.id = requireNonNull(id);
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
        if (obj instanceof PermissionId) {
            PermissionId other = (PermissionId) obj;
            return id.equals(other.id);

        } else {
            return false;
        }
    }

}

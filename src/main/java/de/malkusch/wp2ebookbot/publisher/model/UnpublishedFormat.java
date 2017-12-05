package de.malkusch.wp2ebookbot.publisher.model;

import static java.util.Objects.requireNonNull;

import java.io.File;

public final class UnpublishedFormat {
    final CommentId commentId;

    final FormatId formatId;
    final File file;

    public UnpublishedFormat(CommentId commentId, FormatId formatId, File file) {
        this.commentId = requireNonNull(commentId);
        this.formatId = requireNonNull(formatId);
        this.file = requireNonNull(file);
    }

    public File file() {
        return file;
    }

    public CommentId commentId() {
        return commentId;
    }

    public FormatId formatId() {
        return formatId;
    }

}

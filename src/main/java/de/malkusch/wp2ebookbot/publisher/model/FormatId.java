package de.malkusch.wp2ebookbot.publisher.model;

public enum FormatId {

    EPUB("epub"), MOBI("mobi");

    private final String extension;

    FormatId(String extension) {
        this.extension = extension;
    }

    public String extension() {
        return extension;
    }

}

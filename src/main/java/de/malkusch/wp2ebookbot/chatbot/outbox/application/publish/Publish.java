package de.malkusch.wp2ebookbot.chatbot.outbox.application.publish;

public final class Publish {

    public String permissionId;
    public Format[] formats;

    static public class Format {
        public String id;
        public String url;
    }

}

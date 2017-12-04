package de.malkusch.wp2ebookbot.chatbot.inbox.model;

public interface NLPService {

    void isPermitted(String text, Runnable handler);

    void isRevoked(String text, Runnable handler);

}

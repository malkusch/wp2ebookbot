package de.malkusch.wp2ebookbot.chatbot.model;

public interface NLPService {

    void isPermitted(String text, Runnable handler);

}

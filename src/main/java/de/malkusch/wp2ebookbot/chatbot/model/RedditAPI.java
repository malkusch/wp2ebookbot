package de.malkusch.wp2ebookbot.chatbot.model;

public interface RedditAPI {

    void answerComment(CommentId parent, String response);

    Author self();

}

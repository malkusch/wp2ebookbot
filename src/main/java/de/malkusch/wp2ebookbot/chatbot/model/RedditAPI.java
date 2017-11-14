package de.malkusch.wp2ebookbot.chatbot.model;

public interface RedditAPI {

    public void answerComment(CommentId parent, String response);

}

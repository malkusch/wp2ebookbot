package de.malkusch.wp2ebookbot.chatbot.outbox.model;

public interface AnswerCommentService {

    void answerComment(CommentId parent, String response);

}

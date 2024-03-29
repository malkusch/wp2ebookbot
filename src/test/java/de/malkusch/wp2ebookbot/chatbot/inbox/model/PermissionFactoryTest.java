package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class PermissionFactoryTest {

    private PermissionFactory factory;
    private static final String QUESTION = "May I publish an E-Book?";
    private static final String A_WP_TITLE = "[WP] You write a reddit bot…";
    private static final Author SELF = new Author("http://example.org/u/self");
    private static final InboxMessageContext A_TOP_COMMENT = context("http://example.org/u/arnold",
            "http://example.org/topComment", "Ich saz ûf eime steine", null);

    @Before
    public void setFactory() {
        factory = new PermissionFactory(new PermissionQuestion(QUESTION), SELF);
    }

    @Test
    public void shouldBuildValidPermissionAnswer() {
        InboxMessageContext topComment = A_TOP_COMMENT;
        InboxMessageContext question = context(SELF, "http://example.org/question", QUESTION, topComment);
        InboxMessage answer = message(A_TOP_COMMENT.author, A_WP_TITLE, "Yes", question);

        Collection<Permission> answers = factory.fromInbox(asList(answer));

        Permission expected = new Permission(new PermissionId(answer.id.commentId), A_TOP_COMMENT.commentId,
                answer.message);
        assertEquals(asList(expected), answers);
    }

    @Test
    public void shouldNotBuildWithoutWPTitle() {
        InboxMessageContext topComment = A_TOP_COMMENT;
        InboxMessageContext question = context(SELF, "http://example.org/question", QUESTION, topComment);
        InboxMessage answer = message(A_TOP_COMMENT.author, "title without WP", "Yes", question);

        Collection<Permission> answers = factory.fromInbox(asList(answer));

        assertTrue(answers.isEmpty());
    }

    @Test
    public void shouldNotBuildWithoutQuestion() {
        InboxMessageContext topComment = A_TOP_COMMENT;
        InboxMessage answer = message(A_TOP_COMMENT.author, A_WP_TITLE, "Yes", topComment);

        Collection<Permission> answers = factory.fromInbox(asList(answer));

        assertTrue(answers.isEmpty());
    }

    @Test
    public void shouldNotBuildIfQuestionHasWrongAuthor() {
        InboxMessageContext topComment = A_TOP_COMMENT;
        InboxMessageContext question = context("http://example.org/anotherGuy", "http://example.org/question", QUESTION,
                topComment);
        InboxMessage answer = message(A_TOP_COMMENT.author, A_WP_TITLE, "Yes", question);

        Collection<Permission> answers = factory.fromInbox(asList(answer));

        assertTrue(answers.isEmpty());
    }

    @Test
    public void shouldNotBuildIfQuestionHasWrongQuestion() {
        InboxMessageContext topComment = A_TOP_COMMENT;
        InboxMessageContext question = context(SELF, "http://example.org/question", "Wrong question", topComment);
        InboxMessage answer = message(A_TOP_COMMENT.author, A_WP_TITLE, "Yes", question);

        Collection<Permission> answers = factory.fromInbox(asList(answer));

        assertTrue(answers.isEmpty());
    }

    @Test
    public void shouldNotBuildIfAuthorIsNotFromTopComment() {
        InboxMessageContext topComment = A_TOP_COMMENT;
        InboxMessageContext question = context(SELF, "http://example.org/question", QUESTION, topComment);
        InboxMessage answer = message(new Author("http://example.org/anotherGuy"), A_WP_TITLE, "Yes", question);

        Collection<Permission> answers = factory.fromInbox(asList(answer));

        assertTrue(answers.isEmpty());
    }

    @Test
    public void shouldNotBuildWithoutTopComment() {
        InboxMessageContext topComment = null;
        InboxMessageContext question = context(SELF, "http://example.org/question", QUESTION, topComment);
        InboxMessage answer = message(A_TOP_COMMENT.author, A_WP_TITLE, "Yes", question);

        Collection<Permission> answers = factory.fromInbox(asList(answer));

        assertTrue(answers.isEmpty());
    }

    private static InboxMessageContext context(String author, String commentId, String comment,
            InboxMessageContext context) {

        return context(new Author(author), commentId, comment, context);
    }

    private static InboxMessageContext context(Author author, String commentId, String comment,
            InboxMessageContext context) {

        return new InboxMessageContext(author, new CommentId(new ArticleId("7hsy5b"), commentId), comment,
                Optional.ofNullable(context));
    }

    private static InboxMessage message(Author author, String title, String message, InboxMessageContext context) {

        return new InboxMessage(new InboxMessageId(new CommentId(new ArticleId("7hsy5b"), "dqu6y0p")), author,
                new Title(title), message, Optional.ofNullable(context));
    }
}

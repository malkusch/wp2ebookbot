package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class RevocationFactoryTest {

    private RevocationFactory factory;
    private static final String A_WP_TITLE = "[WP] You write a reddit bot…";
    private static final Author SELF = new Author("http://example.org/u/self");
    private static final InboxMessageContext A_TOP_COMMENT = context("http://example.org/u/arnold",
            "http://example.org/topComment", "Ich saz ûf eime steine", null);

    @Before
    public void setFactory() {
        factory = new RevocationFactory(SELF);
    }

    @Test
    public void shouldBuildValidRevocation() {
        InboxMessageContext topComment = A_TOP_COMMENT;
        InboxMessageContext question = context(SELF, "http://example.org/ask", "may I?", topComment);
        InboxMessageContext permission = context(A_TOP_COMMENT.author, "http://example.org/perm", "yes", question);
        InboxMessageContext publication = context(SELF, "http://example.org/pub", "publication", permission);
        InboxMessage revocation = message(A_TOP_COMMENT.author, A_WP_TITLE, "Revoke", publication);

        Collection<Revocation> revocations = factory.fromInbox(asList(revocation));

        Revocation expected = new Revocation(new RevocationId(revocation.commentId), topComment.commentId,
                revocation.message);
        assertEquals(asList(expected), revocations);
    }

    @Test
    public void shouldNotBuildWithoutWPTitle() {
        InboxMessageContext topComment = A_TOP_COMMENT;
        InboxMessageContext question = context(SELF, "http://example.org/ask", "may I?", topComment);
        InboxMessageContext permission = context(A_TOP_COMMENT.author, "http://example.org/perm", "yes", question);
        InboxMessageContext publication = context(SELF, "http://example.org/pub", "publication", permission);
        InboxMessage revocation = message(A_TOP_COMMENT.author, "title without WP", "Revoke", publication);

        Collection<Revocation> revocations = factory.fromInbox(asList(revocation));

        assertTrue(revocations.isEmpty());
    }

    @Test
    public void shouldNotBuildWithoutPublication() {
        InboxMessageContext topComment = A_TOP_COMMENT;
        InboxMessageContext question = context(SELF, "http://example.org/ask", "may I?", topComment);
        InboxMessageContext permission = context(A_TOP_COMMENT.author, "http://example.org/perm", "yes", question);
        InboxMessage revocation = message(A_TOP_COMMENT.author, A_WP_TITLE, "Revoke", permission);

        Collection<Revocation> revocations = factory.fromInbox(asList(revocation));

        assertTrue(revocations.isEmpty());
    }

    @Test
    public void shouldNotBuildIfPublicationHasWrongAuthor() {
        InboxMessageContext topComment = A_TOP_COMMENT;
        InboxMessageContext question = context(SELF, "http://example.org/ask", "may I?", topComment);
        InboxMessageContext permission = context(A_TOP_COMMENT.author, "http://example.org/perm", "yes", question);
        InboxMessageContext publication = context("http://example.org/anotherGuy", "http://example.org/pub",
                "publication", permission);
        InboxMessage revocation = message(A_TOP_COMMENT.author, A_WP_TITLE, "Revoke", publication);

        Collection<Revocation> revocations = factory.fromInbox(asList(revocation));

        assertTrue(revocations.isEmpty());
    }

    @Test
    public void shouldNotBuildIfAuthorIsNotFromTopComment() {
        InboxMessageContext topComment = A_TOP_COMMENT;
        InboxMessageContext question = context(SELF, "http://example.org/ask", "may I?", topComment);
        InboxMessageContext permission = context(A_TOP_COMMENT.author, "http://example.org/perm", "yes", question);
        InboxMessageContext publication = context(SELF, "http://example.org/pub", "publication", permission);
        InboxMessage revocation = message(new Author("http://example.org/anotherGuy"), A_WP_TITLE, "Revoke",
                publication);

        Collection<Revocation> revocations = factory.fromInbox(asList(revocation));

        assertTrue(revocations.isEmpty());
    }

    @Test
    public void shouldNotBuildWithoutTopComment() {
        InboxMessageContext topComment = null;
        InboxMessageContext question = context(SELF, "http://example.org/ask", "may I?", topComment);
        InboxMessageContext permission = context(A_TOP_COMMENT.author, "http://example.org/perm", "yes", question);
        InboxMessageContext publication = context(SELF, "http://example.org/pub", "publication", permission);
        InboxMessage revocation = message(A_TOP_COMMENT.author, A_WP_TITLE, "Revoke", publication);

        Collection<Revocation> revocations = factory.fromInbox(asList(revocation));

        assertTrue(revocations.isEmpty());
    }

    private static InboxMessageContext context(String author, String commentId, String comment,
            InboxMessageContext context) {

        return context(new Author(author), commentId, comment, context);
    }

    private static InboxMessageContext context(Author author, String commentId, String comment,
            InboxMessageContext context) {

        return new InboxMessageContext(author, new CommentId(commentId), comment, Optional.ofNullable(context));
    }

    private static InboxMessage message(Author author, String title, String message, InboxMessageContext context) {

        return new InboxMessage(new InboxMessageId("http://example.org/message"),
                new CommentId("http://example.org/message"), author, new Title(title), message,
                Optional.ofNullable(context));
    }
}

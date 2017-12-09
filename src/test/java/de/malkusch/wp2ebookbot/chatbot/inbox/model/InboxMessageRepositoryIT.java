package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static de.malkusch.wp2ebookbot.shared.infrastructure.StreamUtil.unchecked;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import de.malkusch.wp2ebookbot.test.IntegrationTest;

@IntegrationTest
@RunWith(SpringRunner.class)
public class InboxMessageRepositoryIT {

    @Autowired
    private InboxMessageRepository messages;

    @Autowired
    private InboxTestService testService;

    @Before
    public void markInboxRead() throws IOException {
        Collection<InboxMessage> newMessages;
        do {
            newMessages = messages.fetchNewMessages();
            newMessages.stream().map(InboxMessage::id).forEach(unchecked(messages::markRead));

        } while (!newMessages.isEmpty());
    }

    @Test
    public void shouldFindNewMessages() throws IOException {
        InboxMessageId unreadMessageId = testService.anyUnreadMessage();
        Collection<InboxMessage> newMessages = messages.fetchNewMessages();
        assertTrue(newMessages.stream().map(InboxMessage::id).anyMatch(unreadMessageId::equals));
    }

    @Test
    public void shouldNotFindReadMessage() throws IOException {
        markInboxRead();
        Collection<InboxMessage> newMessages = messages.fetchNewMessages();
        assertTrue(newMessages.isEmpty());
    }

    @Test
    public void readMessageShouldNotBeFound() throws IOException {
        InboxMessageId messageId = testService.anyUnreadMessage();
        messages.markRead(messageId);
        Collection<InboxMessage> newMessages = messages.fetchNewMessages();
        assertFalse(newMessages.stream().map(InboxMessage::id).anyMatch(messageId::equals));
    }

}

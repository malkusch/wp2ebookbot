package de.malkusch.wp2ebookbot.chatbot.inbox.application;

import java.io.IOException;
import java.util.Collection;

import de.malkusch.wp2ebookbot.chatbot.inbox.model.InboxMessage;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.InboxMessageRepository;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.Permission;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.PermissionFactory;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.PermitPublicationService;

public final class CheckInboxApplicationService {

    CheckInboxApplicationService(InboxMessageRepository inbox, PermissionFactory permissionFactory,
            PermitPublicationService permitService) {

        this.inbox = inbox;
        this.permissionFactory = permissionFactory;
        this.permitService = permitService;
    }

    private final InboxMessageRepository inbox;

    public void checkInbox() throws IOException {
        Collection<InboxMessage> newMessages = inbox.fetchNewMessages();
        checkPermissions(newMessages);
        for (InboxMessage message : newMessages) {
            inbox.markRead(message.id());
        }
    }

    private final PermissionFactory permissionFactory;
    private final PermitPublicationService permitService;

    private void checkPermissions(Collection<InboxMessage> newMessages) {
        Collection<Permission> permissions = permissionFactory.fromInbox(newMessages);
        permissions.forEach(permitService::permitPublication);
    }

}

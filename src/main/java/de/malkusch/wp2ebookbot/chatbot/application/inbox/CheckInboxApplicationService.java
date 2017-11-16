package de.malkusch.wp2ebookbot.chatbot.application.inbox;

import java.io.IOException;
import java.util.Collection;

import de.malkusch.wp2ebookbot.chatbot.model.InboxMessage;
import de.malkusch.wp2ebookbot.chatbot.model.InboxMessageRepository;
import de.malkusch.wp2ebookbot.chatbot.model.Permission;
import de.malkusch.wp2ebookbot.chatbot.model.PermissionFactory;
import de.malkusch.wp2ebookbot.chatbot.model.PermitPublicationService;

public final class CheckInboxApplicationService {

    CheckInboxApplicationService(InboxMessageRepository inbox, PermissionFactory permissionAnswerFactory,
            PermitPublicationService permitService) {

        this.inbox = inbox;
        this.permissionAnswerFactory = permissionAnswerFactory;
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

    private final PermissionFactory permissionAnswerFactory;
    private final PermitPublicationService permitService;

    private void checkPermissions(Collection<InboxMessage> newMessages) {
        Collection<Permission> permissions = permissionAnswerFactory.fromInbox(newMessages);
        permissions.forEach(permitService::publishOnPermission);
    }

}

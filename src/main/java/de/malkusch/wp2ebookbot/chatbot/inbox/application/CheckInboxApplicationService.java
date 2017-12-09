package de.malkusch.wp2ebookbot.chatbot.inbox.application;

import java.io.IOException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.malkusch.wp2ebookbot.chatbot.inbox.model.InboxMessage;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.InboxMessageRepository;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.Permission;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.PermissionFactory;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.PermitPublicationService;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.Revocation;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.RevocationFactory;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.RevokeService;

public final class CheckInboxApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckInboxApplicationService.class);

    CheckInboxApplicationService(InboxMessageRepository inbox, PermissionFactory permissionFactory,
            PermitPublicationService permitService, RevocationFactory revocationFactory, RevokeService revokeService) {

        this.inbox = inbox;
        this.permissionFactory = permissionFactory;
        this.permitService = permitService;
        this.revocationFactory = revocationFactory;
        this.revokeService = revokeService;
    }

    private final InboxMessageRepository inbox;

    public void checkInbox() throws IOException {
        Collection<InboxMessage> newMessages = inbox.fetchNewMessages();
        LOGGER.info("Received {} new messages from inbox", newMessages.size());

        checkPermissions(newMessages);
        checkRevocations(newMessages);

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

    private final RevocationFactory revocationFactory;
    private final RevokeService revokeService;

    private void checkRevocations(Collection<InboxMessage> newMessages) {
        Collection<Revocation> revocations = revocationFactory.fromInbox(newMessages);
        revocations.forEach(revokeService::revokeEBook);
    }

}

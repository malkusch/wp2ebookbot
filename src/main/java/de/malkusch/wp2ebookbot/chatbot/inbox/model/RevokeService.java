package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Objects.requireNonNull;

import de.malkusch.wp2ebookbot.shared.infrastructure.event.PublishEventService;

public final class RevokeService {

    private final NLPService nlpService;
    private final PublishEventService publisher;

    RevokeService(NLPService nlpService, PublishEventService publisher) {
        this.nlpService = nlpService;
        this.publisher = publisher;
    }

    public void revokeEBook(Revocation revocation) {
        requireNonNull(revocation);
        nlpService.isRevoked(revocation.revocation, () -> publisher.publish(new EBookRevoked(revocation)));
    }

}

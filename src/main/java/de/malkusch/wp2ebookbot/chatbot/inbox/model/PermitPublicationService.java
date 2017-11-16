package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static java.util.Objects.requireNonNull;

import de.malkusch.wp2ebookbot.shared.infrastructure.event.PublishEventService;

public final class PermitPublicationService {

    private final NLPService nlpService;
    private final PublishEventService publisher;

    PermitPublicationService(NLPService nlpService, PublishEventService publisher) {
        this.nlpService = nlpService;
        this.publisher = publisher;
    }

    public void permitPublication(Permission answer) {
        requireNonNull(answer);
        nlpService.isPermitted(answer.answer, () -> publisher.publish(new PublicationPermitted(answer)));
    }

}

package de.malkusch.wp2ebookbot.shared.infrastructure.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
final class SpringPublisher implements PublishEventService {

    private final ApplicationEventPublisher publisher;

    SpringPublisher(final ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(Event event) {
        publisher.publishEvent(event);
    }

}

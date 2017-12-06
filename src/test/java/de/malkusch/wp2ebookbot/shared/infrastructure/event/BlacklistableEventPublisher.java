package de.malkusch.wp2ebookbot.shared.infrastructure.event;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
@Primary
public final class BlacklistableEventPublisher implements PublishEventService {

    private final PublishEventService publisher;
    private final Set<Class<?>> blacklist = new HashSet<>();

    BlacklistableEventPublisher(SpringPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(Event event) {
        if (blacklist.contains(event.getClass())) {
            return;
        }
        publisher.publish(event);
    }

    public void blacklist(Class<? extends Event> eventType) {
        blacklist.add(eventType);
    }

}

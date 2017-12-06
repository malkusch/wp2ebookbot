package de.malkusch.wp2ebookbot.shared.infrastructure.event;

import static org.junit.Assert.fail;

import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
final class TestEventListener {

    static class TestEvent implements Event {

    }

    @EventListener(TestEvent.class)
    public void failOnTestEvent(TestEvent event) {
        fail();
    }

}
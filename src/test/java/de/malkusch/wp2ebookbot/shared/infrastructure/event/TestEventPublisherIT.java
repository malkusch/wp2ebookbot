package de.malkusch.wp2ebookbot.shared.infrastructure.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import de.malkusch.wp2ebookbot.test.IntegrationTest;

@IntegrationTest
@RunWith(SpringRunner.class)
public class TestEventPublisherIT {

    @Autowired
    private PublishEventService publisher;

    @Autowired
    private BlacklistableEventPublisher testPublisher;

    static class TestEvent implements Event {

    }

    @Test
    public void shouldNotPublishBlacklistedEvent() {
        testPublisher.blacklist(TestEvent.class);
        publisher.publish(new TestEvent());
    }
}

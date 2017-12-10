package de.malkusch.wp2ebookbot.chatbot.outbox.model;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import de.malkusch.wp2ebookbot.test.IntegrationTest;

@IntegrationTest
@RunWith(SpringRunner.class)
public class WritingPromptRepositoryIT {

    @Autowired
    WritingPromptRepository writingPrompts;

    @Test
    public void shouldFindWritingPrompts() throws IOException {
        WritingPrompSpecification hint = new WritingPrompSpecification(new Votes(1), Duration.ofHours(1), new Words(1),
                new Votes(1));
        TimeWindow window = new TimeWindow(Instant.now().minus(1, ChronoUnit.DAYS), Instant.now());

        WritingPrompt[] newWritingPrompts = writingPrompts.findEligibleWritingPromptsSince(window, hint);

        assertTrue(newWritingPrompts.length > 0);
    }

    @Test
    public void shouldFindNothingSinceNow() throws IOException {
        WritingPrompSpecification hint = new WritingPrompSpecification(new Votes(1), Duration.ofHours(1), new Words(1),
                new Votes(1));
        TimeWindow window = new TimeWindow(Instant.now(), Instant.now());

        WritingPrompt[] newWritingPrompts = writingPrompts.findEligibleWritingPromptsSince(window, hint);

        assertTrue(newWritingPrompts.length == 0);
    }

}

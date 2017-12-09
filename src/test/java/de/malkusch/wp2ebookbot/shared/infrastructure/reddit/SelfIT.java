package de.malkusch.wp2ebookbot.shared.infrastructure.reddit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;

import de.malkusch.wp2ebookbot.test.IntegrationTest;

@IntegrationTest
@RunWith(SpringRunner.class)
public class SelfIT {

    @Autowired
    private Self self;

    @Value("${reddit.auth.username}")
    private String expectedSelf;

    @Test
    public void selfShouldBeAuthenticatedUser() {
        assertEquals(new Self(expectedSelf), self);
    }

}

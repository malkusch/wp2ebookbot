package de.malkusch.wp2ebookbot.shared.infrastructure.reddit;

import static org.junit.Assert.assertTrue;

import java.time.Instant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import de.malkusch.wp2ebookbot.test.IntegrationTest;

@IntegrationTest
@RunWith(SpringRunner.class)
public class RedditAuthenticationServiceIT {

    @Autowired
    private RedditAuthenticationService service;

    @Test
    public void shouldAuthenticate() {
        Authentication authentication = service.getAuthentication();
        assertTrue(authentication.expiration.isAfter(Instant.now()));
    }

}

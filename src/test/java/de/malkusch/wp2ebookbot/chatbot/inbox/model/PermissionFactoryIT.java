package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;

import de.malkusch.wp2ebookbot.test.IntegrationTest;

@IntegrationTest
@RunWith(SpringRunner.class)
public class PermissionFactoryIT {

    @Autowired
    private PermissionFactory factory;

    @Value("${reddit.auth.username}")
    private String self;

    @Test
    public void selfShouldBeAuthenticatedUser() {
        assertEquals(new Author(self), factory.self);
    }

}

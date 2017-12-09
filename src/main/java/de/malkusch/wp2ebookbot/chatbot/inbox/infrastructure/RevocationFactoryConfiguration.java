package de.malkusch.wp2ebookbot.chatbot.inbox.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.malkusch.wp2ebookbot.chatbot.inbox.model.Author;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.RevocationFactory;
import de.malkusch.wp2ebookbot.shared.infrastructure.reddit.Self;

@Configuration
class RevocationFactoryConfiguration {

    @Bean
    RevocationFactory revocationFactory(Self self) {
        return new RevocationFactory(new Author(self.username()));
    }

}

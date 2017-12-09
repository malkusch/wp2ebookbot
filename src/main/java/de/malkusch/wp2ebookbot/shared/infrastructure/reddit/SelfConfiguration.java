package de.malkusch.wp2ebookbot.shared.infrastructure.reddit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SelfConfiguration {

    @Autowired
    private RedditRestTemplate restTemplate;

    @Value("${reddit.self.uri}")
    private String selfEndpoint;

    @Bean
    public Self self() {
        SelfResponse self = restTemplate.getForObject(selfEndpoint, SelfResponse.class);
        return new Self(self.name);
    }

    private static class SelfResponse {
        private String name;
    }

}

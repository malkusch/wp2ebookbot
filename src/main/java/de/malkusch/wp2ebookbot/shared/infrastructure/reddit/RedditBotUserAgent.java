package de.malkusch.wp2ebookbot.shared.infrastructure.reddit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class RedditBotUserAgent {

    private final String userAgent;

    RedditBotUserAgent(@Value("${reddit.userAgent}") String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        return userAgent;
    }

}

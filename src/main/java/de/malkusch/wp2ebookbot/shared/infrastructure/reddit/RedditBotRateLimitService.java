package de.malkusch.wp2ebookbot.shared.infrastructure.reddit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.revinate.guava.util.concurrent.RateLimiter;

@Service
public final class RedditBotRateLimitService {

    private final RateLimiter limiter;

    RedditBotRateLimitService(@Value("${reddit.requestsPerSecond}") int requestsPerSecond) {
        limiter = RateLimiter.create(requestsPerSecond);
    }

    public void limitRate() {
        limiter.acquire();
    }

}

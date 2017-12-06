package de.malkusch.wp2ebookbot.shared.infrastructure.reddit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.revinate.guava.util.concurrent.RateLimiter;

@Service
final class RateLimitService {

    private final RateLimiter limiter;

    RateLimitService(@Value("${reddit.requestsPerSecond}") int requestsPerSecond) {
        limiter = RateLimiter.create(requestsPerSecond);
    }

    public void limitRate() {
        limiter.acquire();
    }

}

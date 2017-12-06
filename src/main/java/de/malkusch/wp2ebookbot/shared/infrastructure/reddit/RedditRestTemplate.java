package de.malkusch.wp2ebookbot.shared.infrastructure.reddit;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public final class RedditRestTemplate {

    private final AuthenticationService authenticationService;
    private final RestTemplate restTemplate;
    private final RateLimitService rateLimiter;

    RedditRestTemplate(AuthenticationService authenticationService, RestTemplate restTemplate,
            RateLimitService rateLimiter) {

        this.authenticationService = authenticationService;
        this.restTemplate = restTemplate;
        this.rateLimiter = rateLimiter;
    }

    public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables)
            throws RestClientException {

        Authentication authentication = authenticationService.getAuthentication();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authentication.toString());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        rateLimiter.limitRate();
        return restTemplate.exchange(url, HttpMethod.GET, entity, responseType, uriVariables).getBody();
    }

}

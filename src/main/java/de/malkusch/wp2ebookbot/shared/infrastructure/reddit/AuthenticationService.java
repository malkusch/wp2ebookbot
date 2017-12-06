package de.malkusch.wp2ebookbot.shared.infrastructure.reddit;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.SECONDS;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Service
final class AuthenticationService {

    private volatile Authentication authentication;
    private final ScheduledExecutorService scheduler;
    private final String username;
    private final String password;
    private final RestTemplate restTemplate;
    private final URI authenticationEndpoint;
    private final RateLimitService rateLimiter;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    AuthenticationService(@Value("${reddit.auth.username}") String username,
            @Value("${reddit.auth.password}") String password, @Value("${reddit.auth.clientId}") String clientId,
            @Value("${reddit.auth.clientSecret}") String clientSecret, CloseableHttpClient httpClient,
            RateLimitService rateLimiter, RestTemplateBuilder builder,
            @Value("${reddit.auth.uri}") URI authenticationEndpoint) throws IOException {

        this.username = username;
        this.password = password;
        this.authenticationEndpoint = authenticationEndpoint;
        this.restTemplate = buildRestTemplate(builder, httpClient, clientId, clientSecret);
        this.rateLimiter = rateLimiter;
        scheduler = Executors.newSingleThreadScheduledExecutor();
        authentication = authenticate();
        scheduleNextRecurringAuthentication();
    }

    private static RestTemplate buildRestTemplate(RestTemplateBuilder builder, CloseableHttpClient httpClient,
            String clientId, String clientSecret) {

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return builder.requestFactory(requestFactory).basicAuthorization(clientId, clientSecret).build();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class AuthenticationResponse {

        private @JsonProperty String access_token;
        private @JsonProperty String token_type;
        private @JsonProperty int expires_in;

    }

    private Authentication authenticate() throws IOException {
        rateLimiter.limitRate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "password");
        map.add("username", username);
        map.add("password", password);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            AuthenticationResponse response = restTemplate.postForObject(authenticationEndpoint, request,
                    AuthenticationResponse.class);
            LOGGER.info("Authenticated for {} seconds", response.expires_in);

            Authentication authentication = new Authentication(response.token_type, response.access_token,
                    now().plusSeconds(response.expires_in));
            return authentication;

        } catch (RestClientException e) {
            throw new IOException(e);
        }
    }

    private void recurringAuthentication() {
        try {
            authentication = authenticate();

        } catch (Exception e) {
            if (authentication.expiration.isAfter(now())) {
                LOGGER.warn("Failed to acquire authentication token", e);
            } else {
                LOGGER.error("Failed to acquire authentication token and old token is expired", e);
            }

        } finally {
            scheduleNextRecurringAuthentication();
        }
    }

    private final static int MIN_DELAY_SECONDS = 60;

    private void scheduleNextRecurringAuthentication() {
        long delay = now().until(authentication.expiration, SECONDS) / 2;
        if (delay < MIN_DELAY_SECONDS) {
            LOGGER.warn("Next authentication renewal would've been in {} seconds, which is less than the minimum delay",
                    delay);
            delay = MIN_DELAY_SECONDS;
        }
        LOGGER.info("Renew authentication token in {} seconds", delay);
        scheduler.schedule(this::recurringAuthentication, delay, TimeUnit.SECONDS);
    }

    public Authentication getAuthentication() {
        return authentication;
    }

}

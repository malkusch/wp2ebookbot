package de.malkusch.wp2ebookbot.shared.infrastructure.reddit;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    public <T> T postForObject(String url, Map<String, ?> data, Class<T> responseType) throws RestClientException {
        return postForObject(URI.create(url), data, responseType);
    }

    public <T> T postForObject(URI url, Map<String, ?> data, Class<T> responseType) throws RestClientException {
        rateLimiter.limitRate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Authentication authentication = authenticationService.getAuthentication();
        headers.add("Authorization", authentication.toString());

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        data.forEach((k, v) -> map.add(k, v.toString()));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        return restTemplate.postForObject(url, request, responseType);
    }

    public <T> T getForObject(String url, Class<T> responseType) throws RestClientException {
        return getForObject(url, responseType, new HashMap<>());
    }

    public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables)
            throws RestClientException {

        rateLimiter.limitRate();

        Authentication authentication = authenticationService.getAuthentication();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authentication.toString());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, entity, responseType, uriVariables).getBody();
    }

}

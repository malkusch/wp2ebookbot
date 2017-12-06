package de.malkusch.wp2ebookbot.shared.infrastructure.reddit;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
class RestConfiguration {

    @Bean
    CloseableHttpClient httpClient(@Value("${reddit.httpClient.maxTotal}") int maxTotal,
            @Value("${reddit.httpClient.maxPerRoute}") int maxPerRoute,
            @Value("${reddit.timeoutSeconds}") int timeoutSeconds, UserAgent userAgent) {

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeoutSeconds * 1000)
                .setConnectTimeout(timeoutSeconds * 1000).build();

        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(maxTotal);
        manager.setDefaultMaxPerRoute(maxPerRoute);

        CloseableHttpClient client = HttpClients.custom().setConnectionManager(manager)
                .setUserAgent(userAgent.toString()).setDefaultRequestConfig(requestConfig).build();

        return client;
    }

    @Bean
    RestTemplate restTemplate(CloseableHttpClient httpClient) {
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

}

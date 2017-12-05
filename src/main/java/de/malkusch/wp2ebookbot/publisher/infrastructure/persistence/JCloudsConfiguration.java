package de.malkusch.wp2ebookbot.publisher.infrastructure.persistence;

import static org.jclouds.Constants.PROPERTY_CONNECTION_TIMEOUT;
import static org.jclouds.Constants.PROPERTY_SO_TIMEOUT;

import java.util.Properties;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStoreContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class JCloudsConfiguration {

    @Bean
    BlobStoreContext context(@Value("${jclouds.provider}") String provider,
            @Value("${jclouds.identity}") String identity, @Value("${jclouds.credential}") String credential,
            @Value("${jclouds.timeoutSeconds}") int timeoutSeconds) {

        int timeoutMilliSeconds = timeoutSeconds * 1000;

        Properties overrides = new Properties();
        overrides.put(PROPERTY_CONNECTION_TIMEOUT, timeoutMilliSeconds);
        overrides.put(PROPERTY_SO_TIMEOUT, timeoutMilliSeconds);
        return ContextBuilder.newBuilder(provider).credentials(identity, credential).overrides(overrides)
                .buildView(BlobStoreContext.class);
    }

}

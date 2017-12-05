package de.malkusch.wp2ebookbot.publisher.infrastructure.EBook;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EBookFactoryConfiguration {

    @Bean
    MOBIFactory mobiFactory(@Value("${mobi.timeoutSeconds}") int timeoutSeconds, PackagedBinaryService unpacker)
            throws IOException, InterruptedException {

        File kindlegen = unpacker.unpack("/bin/kindlegen");
        return new MOBIFactory(timeoutSeconds, kindlegen.getCanonicalPath());
    }

}

package de.malkusch.wp2ebookbot.shared.infrastructure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@Profile("!test")
class SchedulerConfiguration {

}

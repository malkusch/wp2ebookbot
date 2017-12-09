package de.malkusch.wp2ebookbot.chatbot.inbox.infrastructure;

import java.io.IOException;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.malkusch.wp2ebookbot.chatbot.inbox.model.Author;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.PermissionFactory;
import de.malkusch.wp2ebookbot.chatbot.inbox.model.PermissionQuestion;
import de.malkusch.wp2ebookbot.shared.infrastructure.TemplateFactory;
import de.malkusch.wp2ebookbot.shared.infrastructure.reddit.Self;
import freemarker.template.TemplateException;

@Configuration
class PermissionFactoryConfiguration {

    @Bean
    PermissionFactory permissionFactory(Self self) throws IOException, TemplateException {
        return new PermissionFactory(question(), new Author(self.username()));
    }

    @Autowired
    private TemplateFactory templateFactory;

    private PermissionQuestion question() throws TemplateException, IOException {
        try (StringWriter out = new StringWriter()) {
            templateFactory.newTemplate("question.ftl").process(null, out);
            return new PermissionQuestion(out.toString());
        }
    }

}

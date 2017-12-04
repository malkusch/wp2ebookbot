package de.malkusch.wp2ebookbot.shared.infrastructure;

import java.io.IOException;

import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public final class TemplateFactory {

    private final Configuration configuration;

    TemplateFactory() {
        configuration = new Configuration(Configuration.VERSION_2_3_27);
        configuration.setClassForTemplateLoading(getClass(), "/templates");
    }

    public Template newTemplate(String path) throws IOException {
        return configuration.getTemplate(path);
    }

}

package de.malkusch.wp2ebookbot.shared.infrastructure;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateModelException;

@Service
public final class TemplateFactory {

    private final Configuration configuration;

    TemplateFactory(@Value("${master}") String master) throws TemplateModelException {
        configuration = new Configuration(Configuration.VERSION_2_3_27);
        configuration.setSharedVariable("master", master);
        configuration.setClassForTemplateLoading(getClass(), "/templates");
    }

    public Template newTemplate(String path) throws IOException {
        return configuration.getTemplate(path);
    }

}

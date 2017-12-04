package de.malkusch.wp2ebookbot.shared.infrastructure;

import java.io.IOException;

import freemarker.template.Template;

public final class TemplateUtils {

    public static final TemplateFactory FACTORY = new TemplateFactory();

    public static Template newTemplate(String path) throws IOException {
        return FACTORY.newTemplate(path);
    }

}

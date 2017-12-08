package de.malkusch.wp2ebookbot.shared.infrastructure;

import java.io.IOException;

import freemarker.template.Template;
import freemarker.template.TemplateModelException;

public final class TemplateUtils {

    public static final TemplateFactory FACTORY;

    static {
        try {
            FACTORY = new TemplateFactory("/u/malkusch");
        } catch (TemplateModelException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Template newTemplate(String path) throws IOException {
        return FACTORY.newTemplate(path);
    }

}

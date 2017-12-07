package de.malkusch.wp2ebookbot.chatbot.outbox.infrastructure;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import de.malkusch.wp2ebookbot.chatbot.outbox.model.AnswerCommentService;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.Format;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.PublishEBookService;
import de.malkusch.wp2ebookbot.shared.infrastructure.TemplateFactory;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
final class FreemarkerPublishEBookService extends PublishEBookService {

    private final Template template;

    FreemarkerPublishEBookService(AnswerCommentService reddit, TemplateFactory templateFactory) throws IOException {
        super(reddit);
        this.template = templateFactory.newTemplate("publish.ftl");
    }

    @Override
    protected String publishMessage(Format[] formats) {
        Map<String, Object> model = new HashMap<String, Object>();
        Arrays.stream(formats).forEach(f -> model.put(f.id().toString(), f.url()));

        try (StringWriter out = new StringWriter()) {
            template.process(model, out);
            return out.toString();

        } catch (TemplateException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

}

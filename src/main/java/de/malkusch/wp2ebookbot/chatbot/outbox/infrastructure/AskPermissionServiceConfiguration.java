package de.malkusch.wp2ebookbot.chatbot.outbox.infrastructure;

import java.io.IOException;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.malkusch.wp2ebookbot.chatbot.outbox.model.AnswerCommentService;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.AskPermissionService;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.Votes;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.Words;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.WritingPrompSpecification;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.WritingPromptRepository;
import de.malkusch.wp2ebookbot.shared.infrastructure.TemplateFactory;
import freemarker.template.TemplateException;

@Configuration
class AskPermissionServiceConfiguration {

    @Value("${reddit.wp.minWritingPromptVotes}")
    private int minWritingPromptVotes;

    @Value("${reddit.wp.minCommentWords}")
    private int minCommentWords;

    @Value("${reddit.wp.minCommentVotes}")
    private int minCommentVotes;

    @Bean
    public AskPermissionService AskPermissionService(WritingPromptRepository writingPrompts,
            TemplateFactory templateFactory, AnswerCommentService reddit) throws TemplateException, IOException {

        String question;
        try (StringWriter out = new StringWriter()) {
            templateFactory.newTemplate("question.ftl").process(null, out);
            question = out.toString();
        }

        WritingPrompSpecification spec = new WritingPrompSpecification(new Votes(minWritingPromptVotes),
                new Words(minCommentWords), new Votes(minCommentVotes));

        return new AskPermissionService(writingPrompts, spec, question, reddit);
    }

}

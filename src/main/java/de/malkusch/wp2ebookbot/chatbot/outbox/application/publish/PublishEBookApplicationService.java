package de.malkusch.wp2ebookbot.chatbot.outbox.application.publish;

import static java.util.Arrays.stream;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Service;

import de.malkusch.wp2ebookbot.chatbot.outbox.model.CommentId;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.EBook;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.Format;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.FormatId;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.Permission;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.PublishEBookService;

@Service
public final class PublishEBookApplicationService {

    private final PublishEBookService publishService;

    PublishEBookApplicationService(PublishEBookService publishService) {
        this.publishService = publishService;
    }

    public void publish(Publish command) {
        Permission permission = new Permission(new CommentId(command.permissionId));
        Format[] formats = stream(command.formats).map(this::convert).toArray(Format[]::new);
        EBook book = new EBook(permission, formats);

        publishService.publish(book);
    }

    private Format convert(Publish.Format format) {
        try {
            return new Format(new FormatId(format.id), new URL(format.url));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}

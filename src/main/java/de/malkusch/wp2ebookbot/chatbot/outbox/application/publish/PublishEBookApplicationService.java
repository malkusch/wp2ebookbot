package de.malkusch.wp2ebookbot.chatbot.outbox.application.publish;

import static java.util.Arrays.stream;

import java.net.MalformedURLException;
import java.net.URL;

import de.malkusch.wp2ebookbot.chatbot.outbox.model.CommentId;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.EBook;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.Format;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.FormatId;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.Permission;
import de.malkusch.wp2ebookbot.chatbot.outbox.model.PublishEBookService;
import de.malkusch.wp2ebookbot.publisher.model.EBookPublished;

public final class PublishEBookApplicationService {

    private final PublishEBookService publishService;

    PublishEBookApplicationService(PublishEBookService publishService) {
        this.publishService = publishService;
    }

    public void publish(EBookPublished event) {
        Permission permission = new Permission(new CommentId(event.permissionId));
        Format[] formats = stream(event.formats).map(this::convert).toArray(Format[]::new);
        EBook book = new EBook(permission, formats);

        publishService.publish(book);
    }

    private Format convert(EBookPublished.Format format) {
        try {
            return new Format(new FormatId(format.id), new URL(format.url));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}

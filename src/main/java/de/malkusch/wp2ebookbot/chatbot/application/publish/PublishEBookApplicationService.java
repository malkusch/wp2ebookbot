package de.malkusch.wp2ebookbot.chatbot.application.publish;

import static java.util.Arrays.stream;

import java.net.MalformedURLException;
import java.net.URL;

import de.malkusch.wp2ebookbot.chatbot.model.CommentId;
import de.malkusch.wp2ebookbot.chatbot.model.EBook;
import de.malkusch.wp2ebookbot.chatbot.model.Format;
import de.malkusch.wp2ebookbot.chatbot.model.FormatId;
import de.malkusch.wp2ebookbot.chatbot.model.PermissionId;
import de.malkusch.wp2ebookbot.chatbot.model.PublishEBookService;
import de.malkusch.wp2ebookbot.publisher.model.EBookPublished;

public final class PublishEBookApplicationService {

    private final PublishEBookService publishService;

    PublishEBookApplicationService(PublishEBookService publishService) {
        this.publishService = publishService;
    }

    public void publish(EBookPublished event) {
        PermissionId permissionId = new PermissionId(new CommentId(event.permissionId));
        Format[] formats = stream(event.formats).map(this::convert).toArray(Format[]::new);
        EBook book = new EBook(permissionId, formats);

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

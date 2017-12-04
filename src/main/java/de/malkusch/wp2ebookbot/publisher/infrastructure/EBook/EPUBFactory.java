package de.malkusch.wp2ebookbot.publisher.infrastructure.EBook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;

import de.malkusch.wp2ebookbot.publisher.model.Author;
import de.malkusch.wp2ebookbot.publisher.model.CommentId;
import de.malkusch.wp2ebookbot.publisher.model.FormatId;
import de.malkusch.wp2ebookbot.publisher.model.ThreadTitle;
import de.malkusch.wp2ebookbot.publisher.model.UnpublishedFormat;
import de.malkusch.wp2ebookbot.shared.infrastructure.TemplateFactory;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;

final class EPUBFactory {

    private final Template template;

    EPUBFactory(TemplateFactory templateFactory) throws IOException {
        this.template = templateFactory.newTemplate("epub.ftl");
    }

    UnpublishedFormat generateEPUB(CommentId id, ThreadTitle title, Author author, String comment) throws IOException {
        File file = File.createTempFile("wp2ebookbot-", ".epub");
        /*
         * I*m deliberately not calling file.deleteOnExit(). This would create a
         * memory leak, as this bot creates endless files. deleteOnExit()
         * attaches each path to a list which would then grow infinitely. The
         * bot takes care about deletion itself within
         * DeleteTemporaryFormatFileService.
         */

        Book book = new Book();
        book.getMetadata().addTitle(title.toString());
        book.getMetadata().addAuthor(new nl.siegmann.epublib.domain.Author(author.toString()));
        book.addSection("Chapter 1", new Resource(html(title, comment).getBytes("UTF-8"), "chapter1.html"));

        EpubWriter epubWriter = new EpubWriter();

        try (OutputStream out = new FileOutputStream(file)) {
            epubWriter.write(book, out);
        }

        return new UnpublishedFormat(FormatId.EPUB, file);
    }

    private String html(ThreadTitle title, String comment) throws IOException {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("title", title);
        model.put("comment", comment);

        try (StringWriter out = new StringWriter()) {
            try {
                template.process(model, out);

            } catch (TemplateException e) {
                throw new IllegalStateException(e);
            }
            return tidy(out.toString());
        }
    }

    private static String tidy(String html) {
        // This guy is expensive and not
        // thread-safe; object pool maybe
        W3CDom helper = new W3CDom();

        Document doc = Jsoup.parse(html);
        return helper.asString(helper.fromJsoup(doc));
    }

}

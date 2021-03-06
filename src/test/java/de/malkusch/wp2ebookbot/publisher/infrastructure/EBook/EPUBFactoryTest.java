package de.malkusch.wp2ebookbot.publisher.infrastructure.EBook;

import static de.malkusch.wp2ebookbot.publisher.model.ModelAccessUtil.getFile;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.adobe.epubcheck.api.EpubCheck;

import de.malkusch.wp2ebookbot.publisher.model.ArticleId;
import de.malkusch.wp2ebookbot.publisher.model.Author;
import de.malkusch.wp2ebookbot.publisher.model.CommentId;
import de.malkusch.wp2ebookbot.publisher.model.ThreadTitle;
import de.malkusch.wp2ebookbot.publisher.model.UnpublishedFormat;
import de.malkusch.wp2ebookbot.shared.infrastructure.TemplateUtils;

public class EPUBFactoryTest {

    private EPUBFactory factory;

    @Before
    public void setFactory() throws IOException {
        factory = new EPUBFactory(TemplateUtils.FACTORY);
    }

    @Test
    public void shouldGenerateValidEPUB() throws IOException {
        UnpublishedFormat unpublished = factory.generateEPUB(new CommentId(new ArticleId("7hsy5b"), "dqu6y0p"),
                new ThreadTitle("A story"), new Author("A guy"), body("/commentBody.html"));

        EpubCheck epubcheck = new EpubCheck(getFile(unpublished));
        assertTrue(epubcheck.validate());
    }

    @Test
    public void shouldTolerateInvalidCommentHTML() throws IOException {
        UnpublishedFormat unpublished = factory.generateEPUB(new CommentId(new ArticleId("7hsy5b"), "dqu6y0p"),
                new ThreadTitle("A story"), new Author("A guy"), body("/invalidCommentBody.html"));

        EpubCheck epubcheck = new EpubCheck(getFile(unpublished));
        assertTrue(epubcheck.validate());
    }

    static String body(String path) throws IOException {
        return IOUtils.toString(EPUBFactoryTest.class.getResourceAsStream(path), "UTF-8");
    }
}

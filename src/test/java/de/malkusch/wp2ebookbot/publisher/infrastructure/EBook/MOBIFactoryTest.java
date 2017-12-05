package de.malkusch.wp2ebookbot.publisher.infrastructure.EBook;

import static de.malkusch.wp2ebookbot.publisher.infrastructure.EBook.EPUBFactoryTest.body;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import de.malkusch.wp2ebookbot.publisher.model.Author;
import de.malkusch.wp2ebookbot.publisher.model.CommentId;
import de.malkusch.wp2ebookbot.publisher.model.FormatId;
import de.malkusch.wp2ebookbot.publisher.model.ThreadTitle;
import de.malkusch.wp2ebookbot.publisher.model.UnpublishedFormat;
import de.malkusch.wp2ebookbot.shared.infrastructure.TemplateUtils;

public class MOBIFactoryTest {

    private MOBIFactory factory;

    @Before
    public void setFactory() throws IOException, InterruptedException, URISyntaxException {
        File kindlegen = new File(getClass().getResource("/bin/kindlegen").toURI());
        kindlegen.setExecutable(true);
        factory = new MOBIFactory(10, kindlegen.getCanonicalPath());
    }

    @Test
    public void shouldBuildFromEPUB() throws IOException {
        EPUBFactory epubFactory = new EPUBFactory(TemplateUtils.FACTORY);
        UnpublishedFormat epub = epubFactory.generateEPUB(new CommentId("http://example.org"),
                new ThreadTitle("A story"), new Author("A guy"), body("/commentBody.html"));

        UnpublishedFormat mobi = factory.generateMOBI(epub);

        assertTrue(mobi.file().exists());
        assertTrue(mobi.file().length() > 0);
    }

    @Test(expected = IOException.class)
    public void shouldFailOnInvalidBinaryPath() throws IOException, InterruptedException {
        new MOBIFactory(3, "/usr/bin/maBi6ROb");
    }

    @Test(expected = IOException.class)
    public void shouldFailOnInvalidEPUBPath() throws IOException {
        UnpublishedFormat epub = new UnpublishedFormat(FormatId.EPUB, new File("/tmp/FiOtrAy9"));
        factory.generateMOBI(epub);
    }

}

package de.malkusch.wp2ebookbot.publisher.application.publish;

import static java.util.Arrays.stream;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.adobe.epubcheck.api.EpubCheck;
import com.adobe.epubcheck.util.DefaultReportImpl;

import de.malkusch.wp2ebookbot.publisher.model.ArticleId;
import de.malkusch.wp2ebookbot.publisher.model.CommentId;
import de.malkusch.wp2ebookbot.publisher.model.PublishedFormatRepository;
import de.malkusch.wp2ebookbot.test.IntegrationTest;
import de.malkusch.wp2ebookbot.test.UrlAssert;

@IntegrationTest
@RunWith(SpringRunner.class)
public class PublishCommentAsEBookApplicationServiceIT {

    @Autowired
    private PublishCommentAsEBookApplicationService service;

    @Autowired
    private PublishedFormatRepository repository;

    private static final CommentId COMMENT_ID = new CommentId(new ArticleId("7cv1m8"), "dpstonn");

    @Before
    @After
    public void cleanRepository() throws IOException {
        repository.unpublish(COMMENT_ID);
    }

    @Test
    public void shouldPublishComment() throws CommentNotFoundException, IOException {
        PublishComment command = new PublishComment();
        command.articleId = COMMENT_ID.articleId().toString();
        command.commentId = COMMENT_ID.toString();
        command.permissionId = "anything";

        Result result = service.publish(command);
        stream(result.formats).map(f -> f.url).forEach(UrlAssert::assertUrlExists);
        stream(result.formats).filter(f -> f.formatId.equals("EPUB")).map(f -> f.url).forEach(this::assertValidEPUB);
    }

    private void assertValidEPUB(String url) {
        try (InputStream in = new URL(url).openStream()) {
            EpubCheck epubcheck = new EpubCheck(in, new DefaultReportImpl("test"), url);
            assertTrue(epubcheck.validate());

        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

}

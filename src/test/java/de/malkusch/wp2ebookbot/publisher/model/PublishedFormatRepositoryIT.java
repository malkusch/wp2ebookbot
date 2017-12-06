package de.malkusch.wp2ebookbot.publisher.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import de.malkusch.wp2ebookbot.test.IntegrationTest;

@IntegrationTest
@RunWith(SpringRunner.class)
public class PublishedFormatRepositoryIT {

    @Autowired
    private PublishedFormatRepository repository;

    private static final CommentId COMMENT_ID = new CommentId(new ArticleId("7hsy5b"), "dqu6y0p");

    @After
    public void removeCommentId() throws IOException {
        repository.unpublish(COMMENT_ID);
    }

    @Test
    public void shouldPublish() throws IOException {
        UnpublishedFormat unpublished = new UnpublishedFormat(COMMENT_ID, FormatId.EPUB, anyFile());
        PublishedFormat published = repository.publish(unpublished);

        assertUrlExists(published.url);
    }

    @Test
    public void shouldUnpublish() throws IOException {
        UnpublishedFormat unpublished = new UnpublishedFormat(COMMENT_ID, FormatId.EPUB, anyFile());
        PublishedFormat published = repository.publish(unpublished);

        repository.unpublish(COMMENT_ID);

        assertUrlNotExists(published.url);
    }

    @Test
    public void unpublishShouldNotFailIfAlreadyUnpublished() throws IOException {
        repository.unpublish(COMMENT_ID);
        repository.unpublish(COMMENT_ID);
    }

    private static void assertUrlExists(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("HEAD");
        assertEquals(HttpURLConnection.HTTP_OK, con.getResponseCode());
    }

    private static void assertUrlNotExists(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("HEAD");
        assertNotEquals(HttpURLConnection.HTTP_OK, con.getResponseCode());
    }

    private static File anyFile() {
        try {
            return new File(PublishedFormatRepositoryIT.class.getResource("/commentBody.html").toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

}

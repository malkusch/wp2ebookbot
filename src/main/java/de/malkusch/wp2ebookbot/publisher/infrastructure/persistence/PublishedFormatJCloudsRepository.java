package de.malkusch.wp2ebookbot.publisher.infrastructure.persistence;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.BlobAccess;
import org.jclouds.http.UriTemplates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.malkusch.wp2ebookbot.publisher.model.CommentId;
import de.malkusch.wp2ebookbot.publisher.model.FormatId;
import de.malkusch.wp2ebookbot.publisher.model.PublishedFormat;
import de.malkusch.wp2ebookbot.publisher.model.PublishedFormatRepository;
import de.malkusch.wp2ebookbot.publisher.model.UnpublishedFormat;

@Service
final class PublishedFormatJCloudsRepository implements PublishedFormatRepository {

    private final BlobStoreContext context;
    private final String container;
    private final String uriTemplate;

    PublishedFormatJCloudsRepository(BlobStoreContext context, @Value("${jclouds.formats.container}") String container,
            @Value("${jclouds.formats.uritemplate}") String uriTemplate) {

        this.context = context;
        this.container = container;
        this.uriTemplate = uriTemplate;
    }

    @Override
    public PublishedFormat publish(UnpublishedFormat unpublished) throws IOException {
        String key = key(unpublished.commentId(), unpublished.formatId());
        try {
            BlobStore blobStore = context.getBlobStore();
            Blob blob = blobStore.blobBuilder(key).payload(unpublished.file()).build();
            blobStore.putBlob(container, blob);
            blobStore.setBlobAccess(container, key, BlobAccess.PUBLIC_READ);

        } catch (Exception e) {
            throw new IOException(e);
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("container", container);
        variables.put("key", key);
        URL url = new URL(UriTemplates.expand(uriTemplate, variables));
        return new PublishedFormat(unpublished.formatId(), url);
    }

    private static String key(CommentId commentId, FormatId format) {
        return String.format("%s/book.%s", prefix(commentId), format.extension());
    }

    private static String prefix(CommentId commentId) {
        return String.format("%s/%s", commentId.articleId(), commentId);
    }

    @Override
    public void unpublish(CommentId commentId) throws IOException {
        try {
            BlobStore blobStore = context.getBlobStore();
            for (FormatId format : FormatId.values()) {
                blobStore.removeBlob(container, key(commentId, format));
            }
            String prefix = prefix(commentId);
            blobStore.removeBlob(container, prefix);

        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}

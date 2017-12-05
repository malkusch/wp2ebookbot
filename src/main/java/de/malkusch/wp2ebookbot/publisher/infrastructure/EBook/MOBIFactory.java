package de.malkusch.wp2ebookbot.publisher.infrastructure.EBook;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.malkusch.wp2ebookbot.publisher.model.FormatId;
import de.malkusch.wp2ebookbot.publisher.model.UnpublishedFormat;

final class MOBIFactory {

    private final int timeoutSeconds;
    private final String kindlegen;
    private static final Logger LOGGER = LoggerFactory.getLogger(MOBIFactory.class);

    MOBIFactory(int timeoutSeconds, String kindlegen) throws IOException, InterruptedException {
        this.timeoutSeconds = timeoutSeconds;
        this.kindlegen = kindlegen;
        checkKindlegen();
    }

    private void checkKindlegen() throws IOException, InterruptedException {
        Process process = kindlegen("-releasenotes");
        try (InputStream in = new BufferedInputStream(process.getInputStream())) {
            String stdOut = IOUtils.toString(in, "UTF-8");
            if (process.waitFor() != 0) {
                throw new IllegalStateException(String.format("kindlegen is not available: %s", stdOut));
            }
            LOGGER.info("Found kindlegen: {}", stdOut);
        }
    }

    UnpublishedFormat generateMOBI(UnpublishedFormat epub) throws IOException {
        File file = File.createTempFile("wp2ebookbot-", ".mobi");
        Process process = kindlegen("-o", file.getName(), epub.file().getCanonicalPath());
        String stdOut;
        try (InputStream in = new BufferedInputStream(process.getInputStream())) {
            if (!process.waitFor(timeoutSeconds, SECONDS)) {
                process.destroyForcibly().waitFor();
                throw new IOException("kindlegen exceeded timeout");
            }
            stdOut = IOUtils.toString(in, "UTF-8");
            if (process.exitValue() != 1) {
                throw new IOException(String.format("Exit code %d: %s", process.exitValue(), stdOut));
            }
            return new UnpublishedFormat(epub.commentId(), FormatId.MOBI, file);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        }
    }

    private Process kindlegen(String... arguments) throws IOException {
        List<String> effectiveArguments = new ArrayList<>();
        effectiveArguments.add(kindlegen);
        effectiveArguments.addAll(asList(arguments));
        return new ProcessBuilder(effectiveArguments).redirectErrorStream(true).start();
    }

}

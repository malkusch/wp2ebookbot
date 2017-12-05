package de.malkusch.wp2ebookbot.publisher.infrastructure.EBook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

@Service
public final class PackagedBinaryService implements AutoCloseable {

    private final Collection<File> unpackedBinaries = new LinkedList<>();

    public synchronized File unpack(String path) throws IOException {
        File unpacked = File.createTempFile("wp2ebookbot-", ".bin");
        unpackedBinaries.add(unpacked);

        try (InputStream packed = getClass().getResourceAsStream(path);
                OutputStream out = new FileOutputStream(unpacked)) {

            IOUtils.copy(packed, out);
        }

        if (!unpacked.setExecutable(true)) {
            throw new IOException(String.format("Failed to make %s executable", unpacked));
        }
        return unpacked;
    }

    @Override
    public synchronized void close() throws Exception {
        unpackedBinaries.forEach(File::delete);
    }

}

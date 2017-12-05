package de.malkusch.wp2ebookbot.publisher.infrastructure.EBook;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.io.File;

import org.junit.Test;

public class PackagedBinaryServiceTest {

    @Test
    public void shouldUnpack() throws Exception {
        try (PackagedBinaryService unpacker = new PackagedBinaryService()) {
            File bin = unpacker.unpack("/bin/kindlegen");

            assertTrue(bin.exists());
            assertTrue(bin.canExecute());
            assertTrue(bin.length() > 0);
        }
    }

    @Test
    public void shouldCleanup() throws Exception {
        File bin;
        try (PackagedBinaryService unpacker = new PackagedBinaryService()) {
            bin = unpacker.unpack("/bin/kindlegen");
            assumeTrue(bin.exists());
        }
        assertFalse(bin.exists());
    }

}

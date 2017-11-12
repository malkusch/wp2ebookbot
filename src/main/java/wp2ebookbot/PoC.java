/**
 * 
 */
package wp2ebookbot;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;

/**
 *
 * @author Markus Malkusch <markus@malkusch.de>
 */
public class PoC {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		try (InputStream sample = PoC.class.getResourceAsStream("/sample.html")) {
			Book book = new Book();
			book.getMetadata().addTitle(
					"[WP] Seeing success with the purchase of Marvel and now Fox, The Walt Disney Company announces it's next major acquisition: The Catholic Church.");
			book.getMetadata().addAuthor(new Author("https://www.reddit.com/r/TheRobertFall/"));
			book.addSection("Chapter 1", new Resource(sample, "chapter1.html"));

			EpubWriter epubWriter = new EpubWriter();

			try (OutputStream out = new FileOutputStream("/tmp/poc.epub")) {
				epubWriter.write(book, out);
			}
		}
	}

}

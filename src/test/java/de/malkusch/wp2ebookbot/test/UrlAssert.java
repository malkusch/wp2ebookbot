package de.malkusch.wp2ebookbot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlAssert {

    public static void assertUrlExists(String url) {
        try {
            assertUrlExists(new URL(url));
        } catch (MalformedURLException e) {
            throw new AssertionError(e.getMessage(), e);
        }
    }

    public static void assertUrlExists(URL url) {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("HEAD");
            assertEquals(HttpURLConnection.HTTP_OK, con.getResponseCode());

        } catch (IOException e) {
            throw new AssertionError(e.getMessage(), e);
        }
    }

    public static void assertUrlNotExists(String url) {
        try {
            assertUrlNotExists(new URL(url));
        } catch (MalformedURLException e) {
            throw new AssertionError(e.getMessage(), e);
        }
    }

    public static void assertUrlNotExists(URL url) {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("HEAD");
            assertNotEquals(HttpURLConnection.HTTP_OK, con.getResponseCode());
        } catch (IOException e) {
            throw new AssertionError(e.getMessage(), e);
        }
    }

}

package com.carl.crawler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;


public class WebLinkTest {

    final static String DEFAULT_PROTO = "http";
    final static String DEFAULT_HOST = "www.skysports.com";

    @Test
    public void testEmpty() {
        Assertions.assertThrows(
                UnhandledURLException.class,
                () -> WebLink.build("", DEFAULT_PROTO, DEFAULT_HOST), "Empty");
    }

    @Test
    public void testFragmentOnly() {
        Assertions.assertThrows(
                UnhandledURLException.class,
                () -> WebLink.build("#hello-world", DEFAULT_PROTO, DEFAULT_HOST), "Internal link");
    }

    @Test
    public void testQueryOnly() {
        Assertions.assertThrows(
                UnhandledURLException.class,
                () -> WebLink.build("?hello=world", DEFAULT_PROTO, DEFAULT_HOST), "Internal link");
    }

    @Test
    public void testUnsupportedProtocol()  {
        Assertions.assertThrows(
                UnhandledURLException.class,
                () -> WebLink.build("mailto:hello", DEFAULT_PROTO, DEFAULT_HOST), "Unsupported protocol");
    }

    @Test
    public void testTrailingSlashRemoved() throws MalformedURLException, UnhandledURLException {

        URL expectedUrl;

        expectedUrl = new URL("http://www.skysports.com");
        Assertions.assertEquals(
                WebLink.build("http://www.skysports.com/", DEFAULT_PROTO, DEFAULT_HOST), expectedUrl);

        expectedUrl = new URL("http://www.skysports.com/hello");
        Assertions.assertEquals(
                WebLink.build("/hello/", DEFAULT_PROTO, DEFAULT_HOST), expectedUrl);
    }

    @Test
    public void testRelative() throws MalformedURLException, UnhandledURLException {
        URL expectedUrl = new URL("http://www.skysports.com/hello/world");
        Assertions.assertEquals(
                WebLink.build("/hello/world", DEFAULT_PROTO, DEFAULT_HOST), expectedUrl);
    }

//    @Test
//    void testAbsolute() throws URISyntaxException, MalformedURLException {
//        Assertions.assertNull(WebLink.build("www.liverpoolfc.com/news", DEFAULT_PROTO, DEFAULT_HOST));
//    }

    @Test
    public void testFullURL() throws MalformedURLException, UnhandledURLException {
        String url = "https://www.liverpoolfc.com/news";
        URL expectedUrl = new URL(url);
        Assertions.assertEquals(
                WebLink.build(url, DEFAULT_PROTO, DEFAULT_HOST), expectedUrl);
    }
}

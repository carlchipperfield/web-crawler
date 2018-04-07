package com.carl.crawler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class WebLinkTest {

    final String DEFAULT_PROTO = "http";
    final String DEFAULT_HOST = "www.skysports.com";

    @Test
    void testEmpty() {
        Assertions.assertThrows(UnhandledURLException.class, () -> WebLink.build("", DEFAULT_PROTO, DEFAULT_HOST), "Empty");
    }

    @Test
    void testFragmentOnly() {
        Assertions.assertThrows(UnhandledURLException.class, () -> WebLink.build("#hello-world", DEFAULT_PROTO, DEFAULT_HOST), "Internal link");
    }

    @Test
    void testQueryOnly() {
        Assertions.assertThrows(UnhandledURLException.class, () -> WebLink.build("?hello=world", DEFAULT_PROTO, DEFAULT_HOST), "Internal link");
    }

    @Test
    void testUnsupportedProtocol()  {
        Assertions.assertThrows(UnhandledURLException.class, () -> WebLink.build("mailto:hello", DEFAULT_PROTO, DEFAULT_HOST), "Unsupported protocol");
    }

    @Test
    void testRelative() throws MalformedURLException, UnhandledURLException {
        URL expectedURL = new URL("http://www.skysports.com/hello/world");
        Assertions.assertEquals(WebLink.build("/hello/world", DEFAULT_PROTO, DEFAULT_HOST), expectedURL);
    }

//    @Test
//    void testAbsolute() throws URISyntaxException, MalformedURLException {
//        Assertions.assertNull(WebLink.build("www.liverpoolfc.com/news", DEFAULT_PROTO, DEFAULT_HOST));
//    }

    @Test
    void testFullURL() throws MalformedURLException, UnhandledURLException {
        String url = "https://www.liverpoolfc.com/news";
        URL expectedURL = new URL(url);
        Assertions.assertEquals(WebLink.build(url, DEFAULT_PROTO, DEFAULT_HOST), expectedURL);
    }
}

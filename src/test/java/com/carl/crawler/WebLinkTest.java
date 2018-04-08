package com.carl.crawler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class WebLinkTest
{
    @Test
    public void testEmpty() throws MalformedURLException {
        URL baseUrl = new URL("http://www.skysports.com");
        Assertions.assertThrows(
                UnhandledURLException.class,
                () -> WebLink.build("", baseUrl));
    }

    @Test
    public void testFragmentOnly() throws MalformedURLException {
        URL baseUrl = new URL("http://www.skysports.com");
        Assertions.assertThrows(
                UnhandledURLException.class,
                () -> WebLink.build("#hello-world", baseUrl));
    }

    @Test
    public void testQueryOnly() throws MalformedURLException {
        URL baseUrl = new URL("http://www.skysports.com");
        Assertions.assertThrows(
                UnhandledURLException.class,
                () -> WebLink.build("?hello=world", baseUrl));
    }

    @Test
    public void testRelative() throws MalformedURLException, UnhandledURLException {
        URL baseUrl = new URL("http://www.skysports.com/hello");
        Assertions.assertEquals(
                new URL("http://www.skysports.com/hello/world"),
                WebLink.build("world", baseUrl));
    }

    @Test
    public void testRelativeAlternative() throws MalformedURLException, UnhandledURLException {
        URL baseUrl = new URL("http://www.skysports.com/hello");
        Assertions.assertEquals(
                new URL("http://www.skysports.com/hello/world"),
                WebLink.build("./world", baseUrl));
    }

    @Test
    public void testAbsoluteFull() throws MalformedURLException, UnhandledURLException {
        URL baseUrl = new URL("http://www.liverpoolfc.com");
        Assertions.assertEquals(
                new URL("https://www.liverpoolfc.com/news"),
                WebLink.build("https://www.liverpoolfc.com/news", baseUrl));
    }

    @Test
    public void testAbsoluteImplicitProtocol() throws MalformedURLException, UnhandledURLException {
        URL baseUrl = new URL("https://www.liverpoolfc.com");
        Assertions.assertEquals(
                new URL("https://www.liverpoolfc.com/news"),
                WebLink.build("//www.liverpoolfc.com/news", baseUrl));
    }

    @Test
    public void testAbsoluteImplicitDomain() throws MalformedURLException, UnhandledURLException {
        URL baseUrl = new URL("http://www.skysports.com");
        Assertions.assertEquals(
                new URL("http://www.skysports.com"),
                WebLink.build("/", baseUrl));
    }

    @Test
    public void testAbsoluteImplicitDomainWithPath() throws MalformedURLException, UnhandledURLException {
        URL baseUrl = new URL("http://www.skysports.com");
        Assertions.assertEquals(
                new URL("http://www.skysports.com/hello/world"),
                WebLink.build("/hello/world", baseUrl));
    }

    @Test
    public void testUnsupportedProtocol() throws MalformedURLException {
        URL baseUrl = new URL("http://www.skysports.com");
        Assertions.assertThrows(
                UnhandledURLException.class,
                () -> WebLink.build("mailto:hello", baseUrl));
    }

    @Test
    public void testTrailingSlashRemoved() throws MalformedURLException, UnhandledURLException {
        URL baseUrl = new URL("http://www.skysports.com");
        Assertions.assertEquals(
                new URL("http://www.skysports.com"),
                WebLink.build("http://www.skysports.com/", baseUrl));
        Assertions.assertEquals(
                new URL("http://www.skysports.com/hello"),
                WebLink.build("/hello/", baseUrl));
    }
}

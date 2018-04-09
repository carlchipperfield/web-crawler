package com.carl.crawler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;


public class WebLinkTest
{
    @Test
    public void testEmpty() throws MalformedURLException, UnhandledURLException {
        Assertions.assertEquals(
                new URL("http://www.skysports.com"),
                WebLink.build("", new URL("http://www.skysports.com/hello")));
    }

    @Test
    public void testFragmentOnly() throws MalformedURLException, UnhandledURLException {
        Assertions.assertEquals(
                new URL("http://www.skysports.com"),
                WebLink.build("#hello-world", new URL("http://www.skysports.com")));
    }

    @Test
    public void testQueryOnly() throws MalformedURLException, UnhandledURLException {
        Assertions.assertEquals(
                new URL("http://www.skysports.com"),
                WebLink.build("?hello=world", new URL("http://www.skysports.com")));
    }

    @Test
    public void testRelative() throws MalformedURLException, UnhandledURLException {
        Assertions.assertEquals(
                new URL("http://www.skysports.com/world"),
                WebLink.build("world", new URL("http://www.skysports.com/hello")));
    }

    @Test
    public void testRelativeAlternative() throws MalformedURLException, UnhandledURLException {
        Assertions.assertEquals(
                new URL("http://www.skysports.com/world"),
                WebLink.build("./world", new URL("http://www.skysports.com/hello")));
    }

    @Test
    public void testRelativeMulti() throws MalformedURLException, UnhandledURLException {
        Assertions.assertEquals(
                new URL("http://www.skysports.com/1/world"),
                WebLink.build("../../../world", new URL("http://www.skysports.com/1/2/3/4/5")));
    }

    @Test
    public void testAbsoluteFull() throws MalformedURLException, UnhandledURLException {
        Assertions.assertEquals(
                new URL("https://www.liverpoolfc.com/news"),
                WebLink.build("https://www.liverpoolfc.com/news", new URL("http://www.liverpoolfc.com")));
    }

    @Test
    public void testAbsoluteImplicitProtocol() throws MalformedURLException, UnhandledURLException {
        Assertions.assertEquals(
                new URL("https://www.liverpoolfc.com/news"),
                WebLink.build("//www.liverpoolfc.com/news", new URL("https://www.liverpoolfc.com")));
    }

    @Test
    public void testAbsoluteImplicitDomain() throws MalformedURLException, UnhandledURLException {
        Assertions.assertEquals(
                new URL("http://www.skysports.com"),
                WebLink.build("/", new URL("http://www.skysports.com")));
    }

    @Test
    public void testAbsoluteImplicitDomainWithPath() throws MalformedURLException, UnhandledURLException {
        Assertions.assertEquals(
                new URL("http://www.skysports.com/hello/world"),
                WebLink.build("/hello/world", new URL("http://www.skysports.com")));
    }

    @Test
    public void testUnsupportedProtocol() throws MalformedURLException {
        Assertions.assertThrows(
                UnhandledURLException.class,
                () -> WebLink.build("mailto:hello", new URL("http://www.skysports.com")));
    }

    @Test
    public void testTrailingSlashRemoved() throws MalformedURLException, UnhandledURLException {
        Assertions.assertEquals(
                new URL("http://www.skysports.com"),
                WebLink.build("http://www.skysports.com/", new URL("http://www.skysports.com")));
        Assertions.assertEquals(
                new URL("http://www.skysports.com/hello"),
                WebLink.build("/hello/", new URL("http://www.skysports.com")));
    }
}

package com.carl.crawler;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.google.api.client.testing.http.MockHttpTransport;

import java.io.IOException;
import java.net.URL;


public class WebPageTest {

    private static HttpTransport getHttpTransport(String content) {
        return new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() {
                        MockLowLevelHttpResponse result = new MockLowLevelHttpResponse();
                        result.setContent(content);
                        return result;
                    }
                };
            }
        };
    }

    @Test
    public void testPageLoad() throws IOException {
        URL url = new URL("http://www.skysports.com/hello");
        final String content =
                "<head>" +
                "<title>hello</title>" +
                "<meta name=\"description\" content=\"hello world\"" +
                "</head>";

        WebPage expected = new WebPage(url,"hello", "hello world");
        WebPage actual = WebPage.load(url, getHttpTransport(content));

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(actual.getLinks().size(), 0);
    }

    @Test
    public void testLoadEmptyContent() throws IOException {
        URL url = new URL("http://www.skysports.com/hello");

        WebPage expected = new WebPage(url,"", "");
        WebPage actual = WebPage.load(url, getHttpTransport(""));

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(actual.getLinks().size(), 0);
    }

    @Test
    public void testLoadNoDescription() throws IOException {
        URL url = new URL("http://www.skysports.com/hello");
        String content =
                "<head>" +
                "<title>Football</title>" +
                "</head>";

        WebPage expected = new WebPage(url,"Football", "");
        WebPage actual = WebPage.load(url, getHttpTransport(content));

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(actual.getLinks().size(), 0);
    }

    @Test
    public void testLoadOneLink() throws IOException {
        URL url = new URL("http://www.skysports.com/hello");
        String content =
            "<head>" +
            "<title>Football</title>" +
            "<meta name=\"description\" content=\"sky sports\"" +
            "</head>" +
            "<body>" +
            "<a href=\"helloworld\"/>" +
            "</body>";

        WebPage expected = new WebPage(url,"Football", "sky sports");
        WebPage actual = WebPage.load(url, getHttpTransport(content));

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(actual.getLinks().size(), 1);
    }

    @Test
    public void testLoadMultiLinks() throws IOException {
        URL url = new URL("http://www.skysports.com/hello");
        String content =
            "<head>" +
            "<title>Football</title>" +
            "<meta name=\"description\" content=\"sky sports\"" +
            "</head>" +
            "<body>" +
            "<a href=\"helloworld\"/>" +
            "<a href=\"http://sky.com/hello\">Hello</a>" +
            "</body>";

        WebPage expected = new WebPage(url,"Football", "sky sports");
        WebPage actual = WebPage.load(url, getHttpTransport(content));

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(actual.getLinks().size(), 2);
    }
}

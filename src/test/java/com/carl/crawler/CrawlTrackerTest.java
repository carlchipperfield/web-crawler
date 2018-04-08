package com.carl.crawler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class CrawlTrackerTest {

    @Test
    void testIndex() throws MalformedURLException {
        URL url = new URL("http://www.bbc.co.uk");
        CrawlTracker tracker = new CrawlTracker(url, 10);
        tracker.addToIndex(url);
        Assertions.assertTrue(tracker.isCrawled(url));
    }

    @Test
    void testFailed() throws MalformedURLException {
        URL url = new URL("http://www.bbc.co.uk");
        CrawlTracker tracker = new CrawlTracker(url, 10);
        tracker.addToFailed(url);
        Assertions.assertTrue(tracker.isCrawled(url));
    }

    @Test
    void testCrawlLimit() throws MalformedURLException {
        CrawlTracker tracker = new CrawlTracker(new URL("http://www.bbc.co.uk/1"), 5);
        tracker.addToIndex(new URL("http://www.bbc.co.uk/1"));
        tracker.addToIndex(new URL("http://www.bbc.co.uk/2"));
        tracker.addToIndex(new URL("http://www.bbc.co.uk/3"));
        tracker.addToIndex(new URL("http://www.bbc.co.uk/4"));
        Assertions.assertFalse(tracker.isCrawlComplete());
        tracker.addToIndex(new URL("http://www.bbc.co.uk/5"));
        Assertions.assertTrue(tracker.isCrawlComplete());
    }

    @Test
    void testIndexUrlWithSamePathDifferentProtocol() throws MalformedURLException {

        CrawlTracker manager = new CrawlTracker(new URL("http://www.bbc.co.uk"), 10);

        Assertions.assertFalse(manager.isCrawled(new URL("http://www.bbc.co.uk/hello")));
        Assertions.assertFalse(manager.isCrawled(new URL("https://www.bbc.co.uk/hello")));

        manager.addToIndex(new URL("http://www.bbc.co.uk/hello"));

        Assertions.assertTrue(manager.isCrawled(new URL("http://www.bbc.co.uk/hello")));
        Assertions.assertTrue(manager.isCrawled(new URL("https://www.bbc.co.uk/hello")));
    }
}

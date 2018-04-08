package com.carl.crawler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class CrawlManagerTest {

    @Test
    void testIndex() throws MalformedURLException {

        URL defaultUrl = new URL("http://www.bbc.co.uk");
        Integer crawlLimit = 10;

        CrawlManager manager = new CrawlManager(defaultUrl, crawlLimit);

        Assertions.assertFalse(manager.isIndexed(new URL("http://www.bbc.co.uk/hello")));

        manager.addToIndex(new URL("http://www.bbc.co.uk/hello"));

        Assertions.assertTrue(manager.isIndexed(new URL("http://www.bbc.co.uk/hello")));
        Assertions.assertTrue(manager.isIndexed(new URL("https://www.bbc.co.uk/hello")));
    }


}

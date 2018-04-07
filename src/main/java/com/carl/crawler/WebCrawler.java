package com.carl.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class WebCrawler {

    private void start(URL seed, Integer limit) {

        CrawlManager manager = new CrawlManager(seed, limit);

        try {
            WebPage page = WebPage.load(seed);
            System.out.println(page);
            manager.addToIndex(seed);
            page.crawl(manager);
        } catch (IOException exception) {
            System.err.println("Failed to load seed URL");
        }
    }

    public static void main(String[] args) {
        try {
            URL seedUrl = new URL(args[0]);
            Integer crawlLimit = Integer.parseInt(args[1]);

            WebCrawler crawler = new WebCrawler();
            crawler.start(seedUrl, crawlLimit);
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.err.println("Invalid number of arguments");
        } catch (MalformedURLException exception) {
            System.err.println("Invalid seed URL");
        } catch (NumberFormatException exception) {
            System.err.println("Invalid crawl limit");
        }
    }
}

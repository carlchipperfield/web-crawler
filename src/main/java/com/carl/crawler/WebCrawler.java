package com.carl.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class WebCrawler
{
    public static void start(URL seedUrl, Integer crawlLimit) throws IOException {

        CrawlTracker tracker = new CrawlTracker(seedUrl, crawlLimit);

        WebPage page = WebPage.load(seedUrl, WebPage.HTTP_TRANSPORT);
        System.out.println(page);
        tracker.addToIndex(seedUrl);
        page.crawl(tracker);
    }

    public static void main(String[] args) {
        try {
            URL seedUrl = new URL(args[0]);
            Integer crawlLimit = Integer.parseInt(args[1]);

            start(seedUrl, crawlLimit);
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.err.println("Invalid number of arguments");
        } catch (MalformedURLException exception) {
            System.err.println("Invalid seed URL");
        } catch (NumberFormatException exception) {
            System.err.println("Invalid crawl limit");
        } catch (IOException exception) {
            System.err.println("Failed to load seed URL");
        }
    }
}

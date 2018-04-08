package com.carl.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class WebCrawler {

    public static void start(URL seedUrl, Integer crawlLimit) throws IOException {

        CrawlManager manager = new CrawlManager(seedUrl, crawlLimit);

        WebPage page = WebPage.load(seedUrl);
        System.out.println(page);
        manager.addToIndex(seedUrl);
        page.crawl(manager);
    }

    public static void main(String[] args) {
        try {
            URL seedUrl = new URL(args[0]);
            Integer crawlLimit = Integer.parseInt(args[1]);

            WebCrawler.start(seedUrl, crawlLimit);
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

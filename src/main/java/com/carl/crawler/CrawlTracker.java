package com.carl.crawler;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class CrawlTracker {

    private URL seedUrl;
    private Integer remaining;
    private Set<String> index;
    private Set<String> failed;

    public CrawlTracker(URL seedUrl, Integer limit) {
        this.seedUrl = seedUrl;
        this.remaining = limit;
        this.index = new HashSet<>();
        this.failed = new HashSet<>();
    }

    private static String getURL(URL url) {
        return url.getHost() + url.getPath();
    }

    public boolean addToIndex(URL url) {
        remaining--;
        return index.add(getURL(url));
    }

    public boolean addToFailed(URL url) {
        return failed.add(getURL(url));
    }

    public boolean isCrawled(URL url) {
        String cleanedUrl = getURL(url);
        return index.contains(cleanedUrl) || failed.contains(cleanedUrl);
    }

    public boolean isCrawlComplete()
    {
        return remaining == 0;
    }

    public URL getSeedUrl() {
        return seedUrl;
    }
}

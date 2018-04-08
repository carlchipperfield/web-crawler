package com.carl.crawler;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class CrawlTracker
{
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

    private static String getHostWithPath(URL url) {
        return url.getHost() + url.getPath();
    }

    public boolean addToIndex(URL url) {
        remaining--;
        return index.add(getHostWithPath(url));
    }

    public boolean addToFailed(URL url) {
        return failed.add(getHostWithPath(url));
    }

    public boolean isCrawled(URL url) {
        String hostWithPath = getHostWithPath(url);
        return index.contains(hostWithPath) || failed.contains(hostWithPath);
    }

    public boolean isCrawlComplete() {
        return remaining == 0;
    }

    public URL getSeedUrl() {
        return seedUrl;
    }
}

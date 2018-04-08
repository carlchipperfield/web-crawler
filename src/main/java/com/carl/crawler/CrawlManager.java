package com.carl.crawler;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class CrawlManager {

    private URL seedUrl;
    private Integer remaining;
    private Set<String> index;

    public CrawlManager(URL seedUrl, Integer limit) {
        this.seedUrl = seedUrl;
        this.remaining = limit;
        this.index = new HashSet();
    }

    public boolean addToIndex(URL url) {
        remaining--;
        return index.add(url.getHost() + url.getPath());
    }

    public boolean isIndexed(URL url) {
        return index.contains(url.getHost() + url.getPath());
    }

    public boolean isCrawlComplete()
    {
        return remaining == 0;
    }

    public URL getSeedUrl() {
        return seedUrl;
    }
}

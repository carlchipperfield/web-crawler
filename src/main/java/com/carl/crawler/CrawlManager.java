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

        String path = url.getPath();

        if (path.length() > 0 && path.substring(path.length()-1).equals("/")) {
            path = path.substring(0, path.length()-1);
        }

        return index.add(url.getHost() + path);
    }

    public boolean isIndexed(URL url) {
        String path = url.getPath();

        if (path.length() > 0 && path.substring(path.length()-1).equals("/")) {
            path = path.substring(0, path.length()-1);
        }

        return index.contains(url.getHost() + path);
    }

    public boolean isCrawlComplete()
    {
        return remaining == 0;
    }

    public URL getSeedUrl() {
        return seedUrl;
    }
}

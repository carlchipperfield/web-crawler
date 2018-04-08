package com.carl.crawler;

import java.util.List;
import java.util.LinkedList;
import java.io.IOException;
import java.net.URL;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebPage {

    public static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    private URL url;
    private String title;
    private String description;
    private List<URL> links;

    public static WebPage load(URL url, HttpTransport httpTransport) throws IOException {

        HttpResponse resp = httpTransport.createRequestFactory()
            .buildGetRequest(new GenericUrl(url.toString()))
            .setThrowExceptionOnExecuteError(true)
            .execute();

        String content;

        try {
            content = resp.parseAsString();
        } finally {
            resp.disconnect();
        }

        Document doc = Jsoup.parse(content, url.toString());

        Elements description = doc.select("meta[name=description]");
        Elements links = doc.select("a[href]");

        String desc = description.attr("content").trim();

        WebPage webPage = new WebPage(url, doc.title(), desc);

        for (Element link : links) {
            try {
                String href = link.attr("href");
                webPage.addLink(WebLink.build(href, url.getProtocol(), url.getHost()));
            } catch (UnhandledURLException exception) {
//                System.err.println(exception);
            }
        }

        return webPage;
    }

    public void crawl(CrawlTracker tracker) {

        for (URL link : links) {

            if (tracker.isCrawlComplete()) {
                break;
            }

            if (!link.getHost().equals(tracker.getSeedUrl().getHost())) {
                continue;
            }

            if (tracker.isCrawled(link)) {
                continue;
            }

            try {
                WebPage page = WebPage.load(link, WebPage.HTTP_TRANSPORT);
                System.out.println(page);
                tracker.addToIndex(link);
                page.crawl(tracker);
            } catch (IOException exception) {
                tracker.addToFailed(link);
            }
        }
    }

    public WebPage(URL url, String title, String description) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.links = new LinkedList<>();
    }

    public void addLink(URL link) {
        links.add(link);
    }

    public URL getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<URL> getLinks() {
        return links;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof WebPage)) return false;

        final WebPage other = (WebPage)obj;

        return url.equals(other.getUrl())
            && title.equals(other.getTitle())
            && description.equals(other.getDescription());
    }

    @Override
    public String toString() {
        final String eol = System.lineSeparator();
        return title + eol + description + eol;
    }
}

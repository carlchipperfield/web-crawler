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

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    private String title;
    private String description;
    private List<URL> links;

    public static WebPage load(URL url) throws IOException {

        HttpRequestFactory requestFactory =
                HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) {

            }
        });

        GenericUrl genericUrl = new GenericUrl(url.toString());

        HttpRequest request = requestFactory.buildGetRequest(genericUrl);

        HttpResponse resp = request.execute();
        String content = resp.parseAsString();

        Document doc = Jsoup.parse(content, url.toString());

        Elements description = doc.select("meta[name=description]");
        Elements links = doc.select("a[href]");

        String desc = description.attr("content").trim();

        WebPage webPage = new WebPage(doc.title(), desc);

        for (Element link : links) {
            try {
                String href = link.attr("href");
                webPage.addLink(WebLink.build(href, url.getProtocol(), url.getHost()));
            } catch (UnhandledURLException exception) {
                // System.err.println(exception);
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
                WebPage page = WebPage.load(link);
                System.out.println(page);
                tracker.addToIndex(link);
                page.crawl(tracker);
            } catch (IOException e) {
                tracker.addToFailed(link);
            }
        }
    }

    public WebPage(String title, String description) {
        this.title = title;
        this.description = description;
        this.links = new LinkedList<>();
    }

    public void addLink(URL link) {
        links.add(link);
    }

    @Override
    public String toString() {
        final String eol = System.lineSeparator();
        return title + eol + description + eol;
    }
}

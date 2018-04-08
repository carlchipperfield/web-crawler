package com.carl.crawler;

import com.google.api.client.http.*;

import com.google.api.client.http.javanet.NetHttpTransport;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.LinkedList;


public class WebPage {

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    private URL url;
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

        WebPage webPage = new WebPage(url, doc.title(), desc);

        for (Element link : links) {
            try {
                String href = link.attr("href");
                webPage.addLink(WebLink.build(href, url.getProtocol(), url.getHost()));
            } catch (MalformedURLException exception) {
                System.err.println(exception);
            } catch (UnhandledURLException exception) {

            }
        }

        return webPage;
    }

    public void crawl(CrawlManager manager) {

        for (URL link : links) {

            if (manager.isCrawlComplete()) {
                break;
            }

            if (!link.getHost().equals(manager.getSeedUrl().getHost())) {
                continue;
            }

            if (manager.isIndexed(link)) {
                continue;
            }

            try {
                WebPage page = WebPage.load(link);
                System.out.println(page);

                manager.addToIndex(link);
                page.crawl(manager);
            } catch (IOException e) {
                System.err.println(link);
            }
        }
    }

    public WebPage(URL url, String title, String description) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.links = new LinkedList<URL>();
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

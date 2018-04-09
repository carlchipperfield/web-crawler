package com.carl.crawler;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class WebLink
{
    static public URL build(String href, URL parentUrl) throws UnhandledURLException {

        href = href.trim();

        try {
            URI uri = new URI(href);

            uri = parentUrl.toURI().resolve(uri);

            String scheme = uri.getScheme();
            String host = uri.getHost();
            String path = uri.getPath();

            if (!scheme.startsWith("http")) {
                throw new UnhandledURLException("Unsupported protocol: " + uri.getScheme());
            }

            if (path.length() > 0 && path.substring(path.length()-1).equals("/")) {
                // Remove trailing slash
                path = path.substring(0, path.length()-1);
            }

            return new URL(scheme, host, path);

        } catch (URISyntaxException | MalformedURLException exception) {
            throw new UnhandledURLException("Invalid URI: " + exception);
        }
    }
}

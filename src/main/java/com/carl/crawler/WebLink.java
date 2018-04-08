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

            String scheme = uri.getScheme();
            String host = uri.getHost();
            String path = uri.getPath();

            if (scheme == null) {
                scheme = parentUrl.getProtocol();
            }

            if (host == null) {
                host = "";
            }

            if (path == null) {
                path = "";
            }

            if (host.isEmpty() && path.isEmpty()) {
                // query string or fragment only
                throw new UnhandledURLException("Internal link");
            }

            if (!scheme.startsWith("http")) {
                throw new UnhandledURLException("Unsupported protocol: " + scheme);
            }

            if (path.startsWith("./")) {
                // Resolve to relative path
                path = path.substring(2, path.length());
            }

            if (host.isEmpty() && !path.startsWith("/")) {
                // Resolve to relative path
                path = parentUrl.getPath() + "/" + path;
            }

            if (path.length() > 0 && path.substring(path.length()-1).equals("/")) {
                // Remove trailing slash
                path = path.substring(0, path.length()-1);
            }

            if (host.isEmpty()) {
                host = parentUrl.getHost();
            }

            return new URL(scheme, host, path);

        } catch (URISyntaxException | MalformedURLException exception) {
            throw new UnhandledURLException("Invalid URI: " + exception);
        }

    }
}

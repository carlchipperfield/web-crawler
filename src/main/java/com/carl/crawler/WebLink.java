package com.carl.crawler;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class WebLink {

    static public URL build(String href, String defaultProtocol, String defaultHost) throws UnhandledURLException, MalformedURLException {

        href = href.trim();

        if (href.length() == 0) {
            throw new UnhandledURLException("Empty link");
        }

        if (href.startsWith("#") || href.startsWith("?")) {
            throw new UnhandledURLException("Internal link");
        }

        try {
            URI uri = new URI(href);

            String scheme = uri.getScheme();
            String host = uri.getHost();
            String path = uri.getPath();

            if (scheme == null) {
                scheme = defaultProtocol;
            }

            if (!scheme.startsWith("http")) {
                throw new UnhandledURLException("Unsupported protocol: " + scheme);
            }

            if (host == null) {
                host = defaultHost;
            }

            if (path == null) {
                path = "";
            }

            // Remove trailing slash
            if (path.length() > 0 && path.substring(path.length()-1).equals("/")) {
                path = path.substring(0, path.length()-1);
            }

            return new URL(scheme, host, path);

        } catch (URISyntaxException exception) {
            throw new UnhandledURLException("Invalid URI" + exception);
        }

    }
}

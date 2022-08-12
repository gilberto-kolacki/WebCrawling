package com.axreng.backend;

import com.axreng.backend.model.Link;
import com.axreng.backend.utility.CommonUtils;
import com.axreng.backend.utility.HtmlRetriever;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static spark.Spark.stop;

public class WebCrawlingApplication {

    private final Config config;
    private final List<Link> links = new ArrayList<>();
    private Integer RESULTS_ELEMENTS = 0;

    public WebCrawlingApplication(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public void exec() {
        System.out.println("WebCrawlingApplication initialized");
        Link link = new Link(this.getConfig().getBaseUrl(), false);
        links.add(link);
        System.out.printf("Search starting with base URL '%1s' and keyword '%2s'%n", config.getBaseUrl(), config.getKeyword());
        while (CommonUtils.isNotNull(link) && RESULTS_ELEMENTS < config.getMaxResults()) {
            this.checkURL(link);
            links.sort(Comparator.comparing(Link::getUrl));
            link = links.stream().filter(Link::NotChecked).findFirst().orElse(null);
        }
        System.out.printf("Search finished with '%1s' results found %n", links.stream().filter(Link::isValid).count());
    }

    private void checkURL(Link link) {
        String html = new HtmlRetriever().retrieve(link.getUrl());
        try {
            if (CommonUtils.isNotNull(html) && html.contains("<body")) {
                html = html.substring(html.indexOf("<body"), html.indexOf("</body>"));
                this.fetchAnchors(html);
                if(html.toLowerCase().contains(config.getKeyword().toLowerCase())) {
                    System.out.printf("Result found: %1s %n", link.getUrl());
                    link.setValid(true);
                    RESULTS_ELEMENTS++;
                }
            }
        } catch (Exception ex) {
            System.out.println(html);
            ex.printStackTrace();
        }
        link.setChecked(true);
    }

    private void fetchAnchors(String value) {
        Pattern linkPattern = Pattern.compile("href=\"(.*?)\"");
        Matcher linkMatcher = linkPattern.matcher(value);
        while (linkMatcher.find()) {
            String url = this.getUrl(linkMatcher.group(1));
            if (CommonUtils.isNotNull(url) && links.stream().noneMatch((link) -> link.getUrl().equals(url))) {
                links.add(new Link(url, false));
            }
        }
    }

    public String getUrl(String url) {
        try {
            if (CommonUtils.isValidUrl(url)) {
                URI result = new URI(url);
                if (!result.isAbsolute()) {
                    result = new URI(config.getBaseUrl().concat(url));
                }
                if (CommonUtils.urlMatcher(result.toString()) && config.getBaseUrl().contains(result.getHost())) {
                    return result.toString();
                }
            }
            return null;
        } catch (URISyntaxException e) {
            return null;
        }
    }

}

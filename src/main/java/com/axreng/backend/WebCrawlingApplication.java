package com.axreng.backend;

import com.axreng.backend.model.Link;
import com.axreng.backend.utility.CommonUtils;
import com.axreng.backend.utility.HtmlRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static spark.Spark.stop;

public class WebCrawlingApplication {

    private final Config config;
    private static final Logger LOGGER = LoggerFactory.getLogger(WebCrawlingApplication.class);
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
        links.add(new Link(this.getConfig().getBaseUrl(), false));
        System.out.printf("Search starting with base URL '%1s' and keyword '%2s'%n", config.getBaseUrl(), config.getKeyword());
        Link link = links.stream().findFirst().orElse(null);
        while (CommonUtils.isNotNull(link) && RESULTS_ELEMENTS < config.getMaxResults()) {
            this.checkURI(link);
            links.sort(Comparator.comparing(Link::getUrl));
            link =  links.stream().filter(Link::NotChecked).findFirst().orElse(null);
        }
        System.out.printf("Search finished with '%1s' results found %n", links.stream().filter(Link::isValid).count());
        stop();
    }

    private void checkURI(Link link) {
        try {
            HtmlRetriever retriever = new HtmlRetriever();
            String xml = retriever.retrieve(CommonUtils.absoluteUriWithValidScheme(link.getUrl()));
            this.fetchUrls(xml);
            if(xml.toLowerCase().contains(config.getKeyword().toLowerCase())) {
                System.out.printf("Result found: %1s %n", link.getUrl());
                link.setValid(true);
                RESULTS_ELEMENTS++;
            }
            link.setChecked(true);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void fetchUrls(String xml) {
        Scanner xmlScan = new Scanner(xml);
        while (xmlScan.hasNext()) {
            String xmlLine = xmlScan.nextLine();
            if (xmlLine.contains("<a") && xmlLine.contains("href=\"")) {
                String[] anchorArray = xmlLine.split("href=\"");
                for (String href : anchorArray) {
                    String url = this.getAbsoluteUriScheme(config, href.substring(0, href.indexOf("\"")));
                    if (CommonUtils.isNotNull(url) && links.stream().noneMatch((link) -> link.getUrl().equals(url))) {
                        links.add(new Link(url, false));
                    }
                }
            }
        }
        xmlScan.close();
    }

    public String getAbsoluteUriScheme(Config config, String url) {
        try {
            URI result = new URI(url);
            if (!result.isAbsolute()) {
                result = new URI(config.getBaseUrl().concat(url));
            }
            if (!CommonUtils.SUPPORTED_URI_SCHEMES.contains(result.getScheme())) {
                result = new URI(config.getScheme().concat(result.toString()));
            }
            if (!(CommonUtils.urlValidator(result.toString()) && result.toString().contains(config.getBaseUrl()))) {
                return null;
            }
            return result.toString();
        } catch (URISyntaxException e) {
            return null;
        }
    }

}

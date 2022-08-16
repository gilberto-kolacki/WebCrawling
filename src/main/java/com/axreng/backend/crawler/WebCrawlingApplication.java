package com.axreng.backend.crawler;

import com.axreng.backend.Config;
import com.axreng.backend.utility.CommonUtils;

public class WebCrawlingApplication {

    private final Config config;
    private Integer RESULTS_ELEMENTS = 0;

    public WebCrawlingApplication(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public void exec() {
        System.out.println("WebCrawlingApplication initialized");
        WebPage webPage = new WebPage(this.getConfig().getBaseUrl(), false);
        this.getConfig().getLinks().add(webPage);
        System.out.printf("Search starting with base URL '%1s' and keyword '%2s'%n", config.getBaseUrl(), config.getKeyword());
        do {
            if (webPage.webPageContainsKeyword()) {
                System.out.printf("Result found: %1s %n", webPage.getUrl());
                webPage.setValid(true);
                RESULTS_ELEMENTS++;
            }
            webPage = this.getConfig().getLinks().stream().filter(WebPage::NotChecked).findFirst().orElse(null);
        } while (CommonUtils.isNotNull(webPage) && RESULTS_ELEMENTS < config.getMaxResults());
        System.out.printf("Search finished with '%1s' results found %n", this.getConfig().getLinks().stream().filter(WebPage::isValid).count());
    }

}

package com.axreng.backend.crawler;

import com.axreng.backend.Config;
import com.axreng.backend.utility.CommonUtils;
import com.axreng.backend.utility.HtmlRetriever;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebPage {

    private final String url;
    private boolean checked;
    private boolean valid;


    public WebPage(String url, boolean checked) {
        this.url = url;
        this.checked = checked;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean NotChecked() {
        return !checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getUrl() {
        return url;
    }

    private List<WebPage> getLinks() {
        return Config.getInstance().getLinks();
    }

    public boolean webPageContainsKeyword() {
        boolean find = false;
        String html = new HtmlRetriever().retrieve(this.getUrl());
        try {
            if (CommonUtils.isNotNull(html) && html.contains("<body")) {
                html = html.substring(html.indexOf("<body"), html.indexOf("</body>"));
                this.fetchAnchors(html);
                if(html.toLowerCase().contains(Config.getInstance().getKeyword().toLowerCase())) {
                    find = true;
                }
            }
        } catch (Exception ex) {
            System.out.println(html);
            ex.printStackTrace();
        }
        this.setChecked(true);
        return find;
    }

    private void fetchAnchors(String value) {
        Pattern linkPattern = Pattern.compile("href=\"(.*?)\"");
        Matcher linkMatcher = linkPattern.matcher(value);
        while (linkMatcher.find()) {
            String url = this.getUrl(linkMatcher.group(1));
            if (CommonUtils.isNotNull(url) && this.getLinks().stream().noneMatch((link) -> link.getUrl().equals(url))) {
                this.getLinks().add(new WebPage(url, false));
            }
        }
        this.getLinks().sort(Comparator.comparing(WebPage::getUrl));
    }

    private String getUrl(String url) {
        try {
            if (CommonUtils.isValidUrl(url)) {
                Config config = Config.getInstance();
                URI result = new URI(url);
                if (!result.isAbsolute()) {
                    result = new URI(Config.getInstance().getBaseUrl().concat(url));
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

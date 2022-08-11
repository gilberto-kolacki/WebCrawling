package com.axreng.backend.model;

public class Link {

    private final String url;
    private boolean checked;
    private boolean valid;


    public Link(String url, boolean checked) {
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

}

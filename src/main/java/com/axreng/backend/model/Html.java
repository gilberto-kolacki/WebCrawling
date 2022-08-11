package com.axreng.backend.model;

import java.io.Serializable;

public class Html implements Serializable {

    private BodyHtml body;

    public BodyHtml getBody() {
        return body;
    }

    public void setBody(BodyHtml body) {
        this.body = body;
    }
}

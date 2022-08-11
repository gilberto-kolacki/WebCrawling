package com.axreng.backend.model;

import java.io.Serializable;
import java.util.List;

public class BodyHtml implements Serializable {

    private List<AnchorHtml> anchors;

    public List<AnchorHtml> getAnchors() {
        return anchors;
    }

    public void setAnchors(List<AnchorHtml> anchors) {
        this.anchors = anchors;
    }
}

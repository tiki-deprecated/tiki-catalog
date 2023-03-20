/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.index;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IndexAO {
    private String block;
    private String appId;
    private String src;
    private List<IndexAOTitle> titles;
    private List<IndexAOLicense> licenses;

    @JsonCreator
    public IndexAO(
            @JsonProperty(required = true) String block,
            @JsonProperty(required = true) String appId,
            @JsonProperty(required = true) String src,
            @JsonProperty List<IndexAOTitle> titles,
            @JsonProperty List<IndexAOLicense> licenses) {
        this.block = block;
        this.appId = appId;
        this.src = src;
        this.titles = titles;
        this.licenses = licenses;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public List<IndexAOTitle> getTitles() {
        return titles;
    }

    public void setTitles(List<IndexAOTitle> titles) {
        this.titles = titles;
    }

    public List<IndexAOLicense> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<IndexAOLicense> licenses) {
        this.licenses = licenses;
    }
}

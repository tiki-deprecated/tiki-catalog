/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.index;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IndexAOReq {
    private String block;
    private String appId;
    private String src;
    private List<IndexAOReqTitle> titles;
    private List<IndexAOReqLicense> licenses;

    @JsonCreator
    public IndexAOReq(
            @JsonProperty(required = true) String block,
            @JsonProperty(required = true) String appId,
            @JsonProperty(required = true) String src,
            @JsonProperty List<IndexAOReqTitle> titles,
            @JsonProperty List<IndexAOReqLicense> licenses) {
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

    public List<IndexAOReqTitle> getTitles() {
        return titles;
    }

    public void setTitles(List<IndexAOReqTitle> titles) {
        this.titles = titles;
    }

    public List<IndexAOReqLicense> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<IndexAOReqLicense> licenses) {
        this.licenses = licenses;
    }
}

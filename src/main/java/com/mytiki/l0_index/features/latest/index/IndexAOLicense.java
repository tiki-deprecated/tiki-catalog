/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.index;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IndexAOLicense {
    private String transaction;
    private String address;
    private String title;
    private List<IndexAOLicenseUse> uses;

    @JsonCreator
    public IndexAOLicense(
            @JsonProperty(required = true) String transaction,
            @JsonProperty(required = true) String address,
            @JsonProperty(required = true) String title,
            @JsonProperty List<IndexAOLicenseUse> uses) {
        this.transaction = transaction;
        this.address = address;
        this.title = title;
        this.uses = uses;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<IndexAOLicenseUse> getUses() {
        return uses;
    }

    public void setUses(List<IndexAOLicenseUse> uses) {
        this.uses = uses;
    }
}

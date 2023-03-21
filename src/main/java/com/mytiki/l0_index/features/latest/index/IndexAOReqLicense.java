/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.index;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mytiki.l0_index.utilities.AOUse;

import java.time.ZonedDateTime;
import java.util.List;

public class IndexAOReqLicense {
    private String transaction;
    private String address;
    private String title;
    private List<AOUse> uses;
    private ZonedDateTime expiry;

    @JsonCreator
    public IndexAOReqLicense(
            @JsonProperty(required = true) String transaction,
            @JsonProperty(required = true) String address,
            @JsonProperty(required = true) String title,
            @JsonProperty List<AOUse> uses,
            @JsonProperty ZonedDateTime expiry) {
        this.transaction = transaction;
        this.address = address;
        this.title = title;
        this.uses = uses;
        this.expiry = expiry;
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

    public List<AOUse> getUses() {
        return uses;
    }

    public void setUses(List<AOUse> uses) {
        this.uses = uses;
    }

    public ZonedDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(ZonedDateTime expiry) {
        this.expiry = expiry;
    }
}

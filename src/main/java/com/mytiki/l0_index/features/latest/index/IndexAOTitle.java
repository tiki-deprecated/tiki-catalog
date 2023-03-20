/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.index;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IndexAOTitle {
    private String transaction;
    private String address;
    private String ptr;
    private List<String> tags;

    @JsonCreator
    public IndexAOTitle(
            @JsonProperty(required = true) String transaction,
            @JsonProperty(required = true) String address,
            @JsonProperty(required = true) String ptr,
            @JsonProperty List<String> tags) {
        this.transaction = transaction;
        this.address = address;
        this.ptr = ptr;
        this.tags = tags;
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

    public String getPtr() {
        return ptr;
    }

    public void setPtr(String ptr) {
        this.ptr = ptr;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}

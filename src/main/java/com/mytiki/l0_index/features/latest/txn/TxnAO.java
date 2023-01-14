/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.txn;

import java.time.ZonedDateTime;

public class TxnAO<C extends TxnAOContents> {
    private String apiId;
    private String address;
    private String block;
    private String url;
    private String hash;
    private Integer version;
    private ZonedDateTime timestamp;
    private String assetRef;
    private String signature;
    private String contentSchema;
    private C contents;

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getAssetRef() {
        return assetRef;
    }

    public void setAssetRef(String assetRef) {
        this.assetRef = assetRef;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getContentSchema() {
        return contentSchema;
    }

    public void setContentSchema(String contentSchema) {
        this.contentSchema = contentSchema;
    }

    public C getContents() {
        return contents;
    }

    public void setContents(C contents) {
        this.contents = contents;
    }
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.txn;

import java.time.ZonedDateTime;

public class TxnAOConsent implements TxnAOContents {
    private String raw;
    private String ownershipId;
    private String destination;
    private String about;
    private String reward;
    private ZonedDateTime expiry;

    public String getOwnershipId() {
        return ownershipId;
    }

    public void setOwnershipId(String ownershipId) {
        this.ownershipId = ownershipId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public ZonedDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(ZonedDateTime expiry) {
        this.expiry = expiry;
    }

    @Override
    public String getRaw() {
        return raw;
    }

    @Override
    public void setRaw(String raw) {
        this.raw = raw;
    }
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.license;

import com.mytiki.l0_index.utilities.AOSignature;
import com.mytiki.l0_index.utilities.AOUse;

import java.time.ZonedDateTime;
import java.util.List;

public class LicenseAORsp {
    private String id;
    private String title;
    private String user;
    private String address;
    private String description;
    private List<AOUse> uses;
    private ZonedDateTime expiry;
    private String terms;
    private ZonedDateTime timestamp;
    private AOSignature userSignature;
    private AOSignature appSignature;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public AOSignature getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(AOSignature userSignature) {
        this.userSignature = userSignature;
    }

    public AOSignature getAppSignature() {
        return appSignature;
    }

    public void setAppSignature(AOSignature appSignature) {
        this.appSignature = appSignature;
    }
}

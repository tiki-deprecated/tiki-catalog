/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.title;

import com.mytiki.l0_index.utilities.AOSignature;

import java.time.ZonedDateTime;
import java.util.List;

public class TitleAORsp {
    private String id;
    private String ptr;
    private String user;
    private String address;
    private String description;
    private List<String> tags;
    private String origin;
    private ZonedDateTime timestamp;
    private AOSignature userSignature;
    private AOSignature appSignature;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPtr() {
        return ptr;
    }

    public void setPtr(String ptr) {
        this.ptr = ptr;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
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

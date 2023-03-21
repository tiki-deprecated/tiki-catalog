/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.license;

import java.util.List;

public class LicenseAOReq {
    private List<String> tags;
    private List<String> usecases;
    private List<String> destinations;
    private List<String> users;
    private List<String> ptrs;
    private boolean includeAll;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getUsecases() {
        return usecases;
    }

    public void setUsecases(List<String> usecases) {
        this.usecases = usecases;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<String> getPtrs() {
        return ptrs;
    }

    public void setPtrs(List<String> ptrs) {
        this.ptrs = ptrs;
    }

    public boolean getIncludeAll() {
        return includeAll;
    }

    public void setIncludeAll(boolean includeAll) {
        this.includeAll = includeAll;
    }
}

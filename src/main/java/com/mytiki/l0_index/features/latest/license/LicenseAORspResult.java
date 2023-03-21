/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.license;

import java.util.List;

public class LicenseAORspResult {
    String id;
    String ptr;
    List<String> tags;
    List<LicenseAORspUse> uses;

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<LicenseAORspUse> getUses() {
        return uses;
    }

    public void setUses(List<LicenseAORspUse> uses) {
        this.uses = uses;
    }
}

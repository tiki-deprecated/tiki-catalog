/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.license;

import java.util.List;

public class LicenseAORspList {
    Integer approxResults;
    Long nextPageToken;
    List<LicenseAORspResult> results;

    public Integer getApproxResults() {
        return approxResults;
    }

    public void setApproxResults(Integer approxResults) {
        this.approxResults = approxResults;
    }

    public Long getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(Long nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<LicenseAORspResult> getResults() {
        return results;
    }

    public void setResults(List<LicenseAORspResult> results) {
        this.results = results;
    }
}

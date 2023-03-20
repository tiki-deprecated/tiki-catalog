/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.index;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IndexAOLicenseUse {
    private String usecase;
    private String destination;

    @JsonCreator
    public IndexAOLicenseUse(
            @JsonProperty(required = true) String usecase,
            @JsonProperty String destination) {
        this.usecase = usecase;
        this.destination = destination;
    }

    public String getUsecase() {
        return usecase;
    }

    public void setUsecase(String usecase) {
        this.usecase = usecase;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.utilities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AOUse {
    private String usecase;
    private String destination;

    public AOUse() {}

    @JsonCreator
    public AOUse(
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

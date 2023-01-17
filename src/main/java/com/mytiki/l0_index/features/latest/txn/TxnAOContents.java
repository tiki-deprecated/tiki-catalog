/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.txn;

import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = TxnAOOwnership.class),
        @JsonSubTypes.Type(value = TxnAOConsent.class),
        @JsonSubTypes.Type(value = TxnAOUnknown.class)})
public interface TxnAOContents {
    String getRaw();

    void setRaw(String raw);
}

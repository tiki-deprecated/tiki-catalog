/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.txn;

public class TxnAORaw implements TxnAOContents {
    private String raw;

    @Override
    public String getRaw() {
        return raw;
    }

    @Override
    public void setRaw(String raw) {
        this.raw = raw;
    }
}

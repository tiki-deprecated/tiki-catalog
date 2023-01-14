/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.txn;

public enum TxnContentSchema {
    UNKNOWN("unknown", null),
    OWNERSHIP("ownership_nft", 1),
    CONSENT("consent_nft", 0);

    private final String name;
    private final Integer id;

    TxnContentSchema(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public static TxnContentSchema fromName(String name) {
        for (TxnContentSchema e : values()) {
            if (e.name != null && e.name.equals(name)) {
                return e;
            }
        }
        return UNKNOWN;
    }

    public static TxnContentSchema fromId(Integer id) {
        for (TxnContentSchema e : values()) {
            if(e.id != null && e.id.equals(id)) {
                return e;
            }
        }
        return UNKNOWN;
    }
}

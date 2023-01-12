/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.address;

import com.mytiki.l0_index.features.latest.block.BlockPageAO;

public class AddressAO {
    private String appId;

    private String address;

    private BlockPageAO blocks;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BlockPageAO getBlocks() {
        return blocks;
    }

    public void setBlocks(BlockPageAO blocks) {
        this.blocks = blocks;
    }
}

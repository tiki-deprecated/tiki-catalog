/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.app;

import com.mytiki.l0_index.features.latest.address.AddressPageAO;

public class AppAO {
    private String appId;

    private AddressPageAO addresses;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public AddressPageAO getAddresses() {
        return addresses;
    }

    public void setAddresses(AddressPageAO addresses) {
        this.addresses = addresses;
    }
}

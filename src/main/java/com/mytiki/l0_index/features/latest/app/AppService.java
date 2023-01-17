/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.app;

import com.mytiki.l0_index.features.latest.address.AddressService;

public class AppService {

    private final AddressService addressService;

    public AppService(AddressService addressService) {
        this.addressService = addressService;
    }

    public AppAO getApp(String appId, int num, int size){
        AppAO rsp = new AppAO();
        rsp.setAppId(appId);
        rsp.setAddresses(addressService.getAddresses(appId, num, size));
        return rsp;
    }
}

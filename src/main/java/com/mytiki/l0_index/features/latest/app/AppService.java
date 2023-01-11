/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.app;

import com.mytiki.l0_index.features.latest.address.AddressAO;
import com.mytiki.l0_index.features.latest.address.AddressService;

import java.util.List;

public class AppService {

    private final AddressService addressService;

    public AppService(AddressService addressService) {
        this.addressService = addressService;
    }

    public AppAO getApp(String appId){
        List<AddressAO> addresses = addressService.getAddresses(appId);
        AppAO rsp = new AppAO();
        rsp.setAppId(appId);
        rsp.setAddress(addresses.stream().map(AddressAO::getAddress).toList());
        return rsp;
    }
}

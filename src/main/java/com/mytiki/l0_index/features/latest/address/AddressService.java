/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.address;

import java.time.ZonedDateTime;
import java.util.Optional;

public class AddressService {

    private final AddressRepository repository;

    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    public AddressDO insert(String address, String appId){
        Optional<AddressDO> found = repository.findByAddressAndAppId(address, appId);
        if(found.isEmpty()) {
            AddressDO addr = new AddressDO();
            addr.setAddress(address);
            addr.setAppId(appId);
            //addr.setUserId();
            addr.setCreated(ZonedDateTime.now());
            return repository.save(addr);
        }else
            return found.get();
    }

    //fetch userId
}

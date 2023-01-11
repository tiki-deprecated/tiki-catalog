/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.address;

import com.mytiki.l0_index.utilities.B64;

import java.util.List;
import java.util.Optional;

public class AddressService {

    private final AddressRepository repository;

    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    public AddressAO getAddress(String appId, String address){
        byte[] addressBytes = B64.decode(address);
        Optional<AddressDO> found = repository.findByAidAndAddress(appId, addressBytes);
        if(found.isPresent()){
            AddressAO rsp = new AddressAO();
            rsp.setAddress(B64.encode(found.get().getAddress()));
            rsp.setAppId(found.get().getAid());
            return rsp;
        }else return null;
    }

    public List<AddressAO> getAddresses(String address){
        byte[] addressBytes = B64.decode(address);
        List<AddressDO> found = repository.findAllByAddress(addressBytes);
        return found.stream().map(addr -> {
            AddressAO rsp = new AddressAO();
            rsp.setAddress(B64.encode(addr.getAddress()));
            rsp.setAppId(addr.getAid());
            return rsp;
        }).toList();
    }
}

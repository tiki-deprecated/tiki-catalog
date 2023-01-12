/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.address;

import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.utilities.B64;

import java.util.List;
import java.util.Optional;

public class AddressService {

    private final AddressRepository repository;
    private final BlockService blockService;

    public AddressService(AddressRepository repository, BlockService blockService) {
        this.repository = repository;
        this.blockService = blockService;
    }

    public AddressAO getAddress(String appId, String address, int page, int size){
        byte[] addressBytes = B64.decode(address);
        AddressAO rsp = new AddressAO();
        rsp.setAppId(appId);
        Optional<AddressDO> found = repository.findByAidAndAddress(appId, addressBytes);
        if(found.isPresent()){
            rsp.setAddress(B64.encode(found.get().getAddress()));
            rsp.setAppId(found.get().getAid());
            rsp.setBlocks(blockService.page(addressBytes, page, size));
        }
        return rsp;
    }

    public List<AddressAO> getAddresses(String appId){
        List<AddressDO> found = repository.findAllByAid(appId);
        return found.stream().map(addr -> {
            AddressAO rsp = new AddressAO();
            rsp.setAddress(B64.encode(addr.getAddress()));
            rsp.setAppId(addr.getAid());
            return rsp;
        }).toList();
    }
}

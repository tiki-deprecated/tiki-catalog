/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.address;

import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.utilities.B64;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.ZonedDateTime;
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
        Optional<AddressDO> found = repository.findByAppIdAndAddress(appId, addressBytes);
        if(found.isPresent()){
            rsp.setAddress(B64.encode(found.get().getAddress()));
            rsp.setAppId(found.get().getAppId());
            rsp.setBlocks(blockService.page(appId, addressBytes, page, size));
        }
        return rsp;
    }

    public AddressPageAO getAddresses(String appId, int num, int size){
        AddressPageAO rsp = new AddressPageAO();
        Page<AddressDO> page = repository.findAllByAppId(appId, PageRequest.of(num, size));
        rsp.setPage(page.getNumber());
        rsp.setTotalPages(page.getTotalPages());
        rsp.setTotalAddresses(page.getTotalElements());
        rsp.setAddresses(page.get().map(address -> B64.encode(address.getAddress())).toList());
        return rsp;
    }

    public AddressDO getCreate(String appId, byte[] address){
        Optional<AddressDO> found = repository.findByAppIdAndAddress(appId, address);
        if(found.isEmpty()) {
            AddressDO req = new AddressDO();
            req.setAppId(appId);
            req.setAddress(address);
            req.setCreated(ZonedDateTime.now());
            return repository.save(req);
        }else
            return found.get();
    }
}

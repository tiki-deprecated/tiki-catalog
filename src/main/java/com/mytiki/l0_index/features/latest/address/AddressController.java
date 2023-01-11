/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.address;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AddressController {
    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/app/{id}/address/{address}")
    public AddressAO getAddress(
            @PathVariable(name = "id") String id,
            @PathVariable(name = "address") String address) {
        return service.getAddress(id, address);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/address/{address}")
    public List<AddressAO> getAddresses(@PathVariable(name = "address") String address) {
        return service.getAddresses(address);
    }
}

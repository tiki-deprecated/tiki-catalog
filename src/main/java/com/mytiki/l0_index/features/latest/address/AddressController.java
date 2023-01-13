/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.address;

import org.springframework.web.bind.annotation.*;

@RestController
public class AddressController {
    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/app/{id}/address/{address}")
    public AddressAO getAddress(
            @PathVariable(name = "id") String id,
            @PathVariable(name = "address") String address,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "100", required = false) int size) {
        return service.getAddress(id, address, page, size);
    }
}

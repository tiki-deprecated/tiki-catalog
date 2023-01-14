/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.address;

import com.mytiki.spring_rest_api.ApiConstants;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = AddressController.PATH_CONTROLLER)
public class AddressController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE +
            "/app/{id}/address/{address}";
    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public AddressAO getAddress(
            @PathVariable(name = "id") String id,
            @PathVariable(name = "address") String address,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "100", required = false) int size) {
        return service.getAddress(id, address, page, size);
    }
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.address;

import com.mytiki.l0_index.utilities.Constants;
import com.mytiki.spring_rest_api.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "FIND")
@RestController
@RequestMapping(value = AddressController.PATH_CONTROLLER)
public class AddressController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE +
            "app/{id}/address/{address}";
    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @Operation(operationId = Constants.PROJECT_DASH_PATH +  "-address-get",
            summary = "Page Blocks", description = "Get a page of block hashes given an app id and address",
            security = @SecurityRequirement(name = "jwt"))
    @RequestMapping(method = RequestMethod.GET)
    public AddressAO getAddress(
            @PathVariable(name = "id") String id,
            @PathVariable(name = "address") String address,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "100", required = false) int size) {
        return service.getAddress(id, address, page, size);
    }
}

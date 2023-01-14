/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.block;

import com.mytiki.spring_rest_api.ApiConstants;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = BlockController.PATH_CONTROLLER)
public class BlockController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE +
            "/app/{id}/address/{address}/block/{hash}";

    private final BlockService service;

    public BlockController(BlockService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public BlockAO getBlock(
            @PathVariable(name = "id") String id,
            @PathVariable(name = "address") String address,
            @PathVariable(name = "hash") String hash) {
        return service.getBlock(id, address, hash);
    }
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.block;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlockController {

    private final BlockService service;

    public BlockController(BlockService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/app/{id}/address/{address}/block/{hash}")
    public BlockAO getBlock(
            @PathVariable(name = "id") String id,
            @PathVariable(name = "address") String address,
            @PathVariable(name = "hash") String hash) {
        return service.getBlock(id, address, hash);
    }
}

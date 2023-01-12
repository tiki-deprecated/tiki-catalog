/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.block;

import com.mytiki.l0_index.utilities.B64;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class BlockService {

    private final BlockRepository repository;

    public BlockService(BlockRepository repository) {
        this.repository = repository;
    }

    public BlockPageAO page(byte[] address, int num, int size){
        Page<BlockDO> page = repository.findAllByAddressAddress(address, PageRequest.of(num, size));
        BlockPageAO rsp = new BlockPageAO();
        rsp.setPage(page.getNumber());
        rsp.setTotalPages(page.getTotalPages());
        rsp.setTotalHashes(page.getTotalElements());
        rsp.setHashes(page.getContent().stream().map(block -> B64.encode(block.getHash())).toList());
        return rsp;
    }
}

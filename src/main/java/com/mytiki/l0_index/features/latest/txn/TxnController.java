/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.txn;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TxnController {
    private final TxnService service;

    public TxnController(TxnService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/app/{id}/address/{address}/block/{block-hash}/transaction/{txn-hash}")
    public TxnAO getTransaction(
            @PathVariable(name = "id") String id,
            @PathVariable(name = "address") String address,
            @PathVariable(name = "block-hash") String blockHash,
            @PathVariable(name = "txn-hash") String txnHash) {
        return service.getTransaction(id, address, blockHash, txnHash);
    }
}

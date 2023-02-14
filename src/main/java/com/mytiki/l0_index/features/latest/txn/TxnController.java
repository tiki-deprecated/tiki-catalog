/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.txn;

import com.mytiki.l0_index.utilities.Constants;
import com.mytiki.spring_rest_api.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FIND")
@RestController
@RequestMapping(value = TxnController.PATH_CONTROLLER)
public class TxnController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE +
            "app/{app-id}/address/{address}/block/{block-hash}/transaction/{txn-hash}";
    private final TxnService service;

    public TxnController(TxnService service) {
        this.service = service;
    }
    @Operation(
            operationId = Constants.PROJECT_DASH_PATH +  "-txn-get",
            summary = "Get Transaction",
            description = "Get a deserialized transaction given an app id, address, block hash, and transaction hash",
            security = @SecurityRequirement(name = "jwt"))
    @RequestMapping(method = RequestMethod.GET)
    public TxnAO getTransaction(
            @PathVariable(name = "app-id") String appId,
            @PathVariable(name = "address") String address,
            @PathVariable(name = "block-hash") String blockHash,
            @PathVariable(name = "txn-hash") String txnHash) {
        return service.getTransaction(appId, address, blockHash, txnHash);
    }
}

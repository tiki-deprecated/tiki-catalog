/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.report;

import com.mytiki.l0_index.features.latest.address.AddressDO;
import com.mytiki.l0_index.features.latest.address.AddressService;
import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.features.latest.txn.TxnService;
import com.mytiki.l0_index.utilities.B64;
import com.mytiki.spring_rest_api.ApiExceptionBuilder;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import java.net.MalformedURLException;
import java.net.URL;

public class ReportService {
    private final AddressService addressService;
    private final BlockService blockService;
    private final TxnService txnService;

    public ReportService(AddressService addressService, BlockService blockService, TxnService txnService) {
        this.addressService = addressService;
        this.blockService = blockService;
        this.txnService = txnService;
    }

    @Transactional
    public void report(ReportAO req){
        if(req.getTransactions().isEmpty()){
            throw new ApiExceptionBuilder(HttpStatus.BAD_REQUEST)
                    .message("Invalid Request")
                    .detail("Empty transaction list")
                    .build();
        }
        try {
            AddressDO address = addressService.getCreate(req.getApiId(), B64.decode(req.getAddress()));
            BlockDO block = blockService.create(B64.decode(req.getBlock()), new URL(req.getSrc()), address);
            req.getTransactions().forEach(txn -> txnService.create(B64.decode(txn), block));
        } catch (MalformedURLException e) {
            throw new ApiExceptionBuilder(HttpStatus.BAD_REQUEST)
                    .message("Invalid Request")
                    .detail("src must be a valid URL")
                    .build();
        } catch (DataIntegrityViolationException e){
            throw new ApiExceptionBuilder(HttpStatus.BAD_REQUEST)
                    .message("Invalid Request")
                    .detail("Integrity violation. Duplicate request.")
                    .help(e.getMessage())
                    .build();
        }
    }
}

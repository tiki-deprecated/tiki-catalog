/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.report;

import com.mytiki.l0_index.features.latest.address.AddressService;
import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.features.latest.txn.TxnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class ReportConfig {
    @Bean
    public ReportService reportService(
            @Autowired AddressService addressService,
            @Autowired BlockService blockService,
            @Autowired TxnService txnService){
        return new ReportService(addressService, blockService, txnService);
    }

    @Bean
    public ReportController reportController(@Autowired ReportService service){
        return new ReportController(service);
    }

}

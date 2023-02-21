/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.search;

import com.mytiki.l0_index.features.latest.address.AddressService;
import com.mytiki.l0_index.features.latest.app.AppService;
import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.features.latest.txn.TxnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class SearchConfig {

    @Bean
    public SearchController searchController(@Autowired SearchService service){
        return new SearchController(service);
    }

    @Bean
    public SearchService searchService(
            @Autowired AppService appService,
            AddressService addressService,
            BlockService blockService,
            TxnService txnService){
        return new SearchService(appService, addressService, blockService, txnService);
    }
}

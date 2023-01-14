/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.txn;

import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(TxnConfig.PACKAGE_PATH)
@EntityScan(TxnConfig.PACKAGE_PATH)
public class TxnConfig {
    public static final String PACKAGE_PATH = Constants.PACKAGE_FEATURES_LATEST_DOT_PATH + ".txn";

    @Bean
    public TxnService txnService(@Autowired TxnRepository repository, @Autowired BlockService blockService){
        return new TxnService(repository, blockService);
    }

    @Bean
    public TxnController txnController(@Autowired TxnService service){
        return new TxnController(service);
    }
}

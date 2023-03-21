/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.title;

import com.mytiki.l0_index.features.latest.address.AddressService;
import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.features.latest.tag.TagService;
import com.mytiki.l0_index.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(TitleConfig.PACKAGE_PATH)
@EntityScan(TitleConfig.PACKAGE_PATH)
public class TitleConfig {
    public static final String PACKAGE_PATH = Constants.PACKAGE_FEATURES_LATEST_DOT_PATH + ".title";

    @Bean
    public TitleService titleService(
            @Autowired TitleRepository repository,
            @Autowired TagService tagService,
            @Autowired AddressService addressService,
            @Autowired BlockService blockService){
        return new TitleService(repository, tagService, addressService, blockService);
    }
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.index;

import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.features.latest.license.LicenseService;
import com.mytiki.l0_index.features.latest.title.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class IndexConfig {
    @Bean
    public IndexService indexService(
            @Autowired BlockService blockService,
            @Autowired TitleService titleService,
            @Autowired LicenseService licenseService){
        return new IndexService(blockService, titleService, licenseService);
    }

    @Bean
    public IndexController indexController(@Autowired IndexService service){
        return new IndexController(service);
    }

}

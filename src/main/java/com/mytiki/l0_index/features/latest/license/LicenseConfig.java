/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.license;

import com.mytiki.l0_index.features.latest.address.AddressService;
import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.features.latest.count.CountService;
import com.mytiki.l0_index.features.latest.title.TitleService;
import com.mytiki.l0_index.features.latest.use.UseService;
import com.mytiki.l0_index.utilities.Constants;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(LicenseConfig.PACKAGE_PATH)
@EntityScan(LicenseConfig.PACKAGE_PATH)
public class LicenseConfig {
    public static final String PACKAGE_PATH = Constants.PACKAGE_FEATURES_LATEST_DOT_PATH + ".license";

    @Bean
    public LicenseController licenseController(@Autowired LicenseService service){
        return new LicenseController(service);
    }

    @Bean
    public LicenseService licenseService(
            @Autowired LicenseRepository repository,
            @Autowired UseService useService,
            @Autowired TitleService titleService,
            @Autowired AddressService addressService,
            @Autowired CountService countService,
            @Autowired BlockService blockService){
        return new LicenseService(repository, useService, titleService, addressService, countService, blockService);
    }

    @Bean
    public LicenseRepositorySearch licenseRepositorySearch(@Autowired EntityManager em){
        return new LicenseRepositorySearchImpl(em);
    }
}

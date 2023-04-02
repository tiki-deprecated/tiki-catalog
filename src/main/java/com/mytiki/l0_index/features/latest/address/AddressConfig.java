/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.address;

import com.mytiki.l0_index.features.latest.registry.RegistryService;
import com.mytiki.l0_index.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.net.URI;

@EnableJpaRepositories(AddressConfig.PACKAGE_PATH)
@EntityScan(AddressConfig.PACKAGE_PATH)
public class AddressConfig {
    public static final String PACKAGE_PATH = Constants.PACKAGE_FEATURES_LATEST_DOT_PATH + ".address";

    @Bean
    public AddressService addressService(
            @Autowired AddressRepository repository,
            @Autowired RegistryService registryService){
        return new AddressService(repository, registryService);
    }
}

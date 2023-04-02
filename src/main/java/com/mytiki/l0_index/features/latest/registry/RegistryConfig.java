/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.registry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

public class RegistryConfig {

    @Bean
    public RegistryService registryService(
            @Value("${com.mytiki.l0_index.l0_registry.id}") String l0RegistryId,
            @Value("${com.mytiki.l0_index.l0_registry.secret}") String l0RegistrySecret,
            @Value("${com.mytiki.l0_index.l0_registry.uri}") String l0RegistryUri,
            @Autowired RestTemplateBuilder restTemplateBuilder){
        return new RegistryService(restTemplateBuilder
                .rootUri(l0RegistryUri)
                .basicAuthentication(l0RegistryId, l0RegistrySecret)
                .build());
    }
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.app;

import com.mytiki.l0_index.features.latest.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class AppConfig {
    @Bean
    public AppController appController(@Autowired AppService service){
        return new AppController(service);
    }

    @Bean
    public AppService appService(@Autowired AddressService addressService){
        return new AppService(addressService);
    }
}

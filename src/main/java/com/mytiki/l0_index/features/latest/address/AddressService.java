/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.address;

import com.mytiki.l0_index.features.latest.registry.RegistryService;
import com.mytiki.l0_index.utilities.B64;
import com.mytiki.l0_index.utilities.Decode;
import com.mytiki.l0_index.utilities.Sha256;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public class AddressService {

    private final AddressRepository repository;
    private final RegistryService registryService;

    public AddressService(AddressRepository repository, RegistryService registryService) {
        this.repository = repository;
        this.registryService = registryService;
    }

    public AddressDO insert(String address, String appId){
        Optional<AddressDO> found = repository.findByAddressAndAppId(address, appId);
        if(found.isEmpty()) {
            AddressDO addr = new AddressDO();
            addr.setAddress(address);
            addr.setAppId(appId);
            addr.setUserId(registryService.getId(address, appId));
            addr.setCreated(ZonedDateTime.now());
            return repository.save(addr);
        }else
            return found.get();
    }
}

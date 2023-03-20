/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.license;

import com.mytiki.l0_index.features.latest.address.AddressService;
import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.index.IndexAOLicense;
import com.mytiki.l0_index.features.latest.title.TitleService;
import com.mytiki.l0_index.features.latest.use.UseService;
import jakarta.transaction.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

public class LicenseService {
    private final LicenseRepository repository;
    private final UseService useService;
    private final TitleService titleService;
    private final AddressService addressService;

    public LicenseService(
            LicenseRepository repository,
            UseService useService,
            TitleService titleService,
            AddressService addressService) {
        this.repository = repository;
        this.useService = useService;
        this.titleService = titleService;
        this.addressService = addressService;
    }

    @Transactional
    public LicenseDO insert(IndexAOLicense req, String appId, BlockDO block){
        Optional<LicenseDO> found = repository.getByTransaction(req.getTransaction());
        if(found.isEmpty()) {
            LicenseDO license = new LicenseDO();
            license.setTransaction(req.getTransaction());
            license.setTitle(titleService.getByTransaction(req.getTitle()));
            license.setUses(useService.insert(req.getUses()));
            license.setAddress(addressService.insert(req.getAddress(), appId));
            license.setBlock(block);
            license.setCreated(ZonedDateTime.now());
            return repository.save(license);
        }else
            return found.get();
    }
}

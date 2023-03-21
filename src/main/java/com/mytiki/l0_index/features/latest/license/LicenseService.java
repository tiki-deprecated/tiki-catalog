/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.license;

import com.mytiki.l0_index.features.latest.address.AddressService;
import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.count.CountService;
import com.mytiki.l0_index.features.latest.index.IndexAOReqLicense;
import com.mytiki.l0_index.features.latest.tag.TagDO;
import com.mytiki.l0_index.features.latest.title.TitleDO;
import com.mytiki.l0_index.features.latest.title.TitleService;
import com.mytiki.l0_index.features.latest.use.UseService;
import com.mytiki.l0_index.utilities.AOUse;
import jakarta.transaction.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public class LicenseService {
    private final LicenseRepository repository;
    private final UseService useService;
    private final TitleService titleService;
    private final AddressService addressService;
    private final CountService countService;

    public LicenseService(
            LicenseRepository repository,
            UseService useService,
            TitleService titleService,
            AddressService addressService,
            CountService countService) {
        this.repository = repository;
        this.useService = useService;
        this.titleService = titleService;
        this.addressService = addressService;
        this.countService = countService;
    }

    @Transactional
    public LicenseDO insert(IndexAOReqLicense req, String appId, BlockDO block){
        Optional<LicenseDO> found = repository.getByTransaction(req.getTransaction());
        if(found.isEmpty()) {
            LicenseDO license = new LicenseDO();
            license.setTransaction(req.getTransaction());
            license.setUses(useService.insert(req.getUses()));
            license.setAddress(addressService.insert(req.getAddress(), appId));
            license.setBlock(block);
            license.setCreated(ZonedDateTime.now());

            TitleDO title = titleService.getByTransaction(req.getTitle());
            if(title != null){
                license.setTitle(title);
                license.setLatest(true);
                repository.clearLatest(title.getId());
            }

            return repository.save(license);
        }else
            return found.get();
    }

    public LicenseAORspList list(LicenseAOReq req, String appId, Long pageToken, Integer maxResults){
        LicenseAORspList rsp = new LicenseAORspList();
        List<LicenseDO> licenses = repository.search(req, appId, pageToken, maxResults);

        if(pageToken == null && licenses.size() < maxResults)
            rsp.setApproxResults(licenses.size());
        else{
            Float total = countService.get("license");
            if(total != null && !licenses.isEmpty()) {
                Long delta = (licenses.get(licenses.size() - 1).getId() - licenses.get(0).getId()) + 1;
                rsp.setApproxResults(Math.round((total / delta) * licenses.size()));
            }
            if(licenses.size() >= maxResults)
                rsp.setNextPageToken(licenses.get(licenses.size()-1).getId());
        }

        rsp.setResults(licenses.stream().map(license -> {
            LicenseAORspResult res = new LicenseAORspResult();
            res.setId(license.getTransaction());
            res.setPtr(license.getTitle().getPtr());
            res.setTags(license.getTitle().getTags().stream().map(TagDO::getValue).toList());
            res.setUses(license.getUses().stream().map(use -> {
                AOUse aoUse = new AOUse();
                aoUse.setUsecase(use.getUsecase());
                aoUse.setDestination(use.getDestination());
                return aoUse;
            }).toList());
            return res;
        }).toList());

        return rsp;
    }
}

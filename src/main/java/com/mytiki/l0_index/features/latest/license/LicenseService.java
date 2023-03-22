/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.license;

import com.mytiki.l0_index.features.latest.address.AddressDO;
import com.mytiki.l0_index.features.latest.address.AddressService;
import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.features.latest.count.CountService;
import com.mytiki.l0_index.features.latest.index.IndexAOReqLicense;
import com.mytiki.l0_index.features.latest.tag.TagDO;
import com.mytiki.l0_index.features.latest.title.TitleDO;
import com.mytiki.l0_index.features.latest.title.TitleService;
import com.mytiki.l0_index.features.latest.use.UseService;
import com.mytiki.l0_index.utilities.AOSignature;
import com.mytiki.l0_index.utilities.AOUse;
import com.mytiki.l0_index.utilities.B64;
import com.mytiki.l0_index.utilities.Decode;
import com.mytiki.spring_rest_api.ApiExceptionBuilder;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public class LicenseService {
    private final LicenseRepository repository;
    private final UseService useService;
    private final TitleService titleService;
    private final AddressService addressService;
    private final CountService countService;
    private final BlockService blockService;

    public LicenseService(
            LicenseRepository repository,
            UseService useService,
            TitleService titleService,
            AddressService addressService,
            CountService countService,
            BlockService blockService) {
        this.repository = repository;
        this.useService = useService;
        this.titleService = titleService;
        this.addressService = addressService;
        this.countService = countService;
        this.blockService = blockService;
    }

    @Transactional
    public LicenseDO insert(IndexAOReqLicense req, String appId, BlockDO block){
        Optional<LicenseDO> found = repository.getByTransaction(req.getTransaction());
        if(found.isEmpty()) {
            LicenseDO license = new LicenseDO();
            license.setTransaction(req.getTransaction());
            license.setExpiry(req.getExpiry());
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

    @Transactional
    public LicenseAORsp fetch(String transaction, String appId){
        LicenseAORsp rsp = new LicenseAORsp();
        Optional<LicenseDO> found = repository.getByTransaction(transaction);
        if(found.isPresent()){
            rsp.setId(transaction);
            rsp.setTitle(found.get().getTitle().getTransaction());
            rsp.setExpiry(found.get().getExpiry());
            rsp.setUses(found.get().getUses().stream().map(use -> {
                AOUse aoUse = new AOUse();
                aoUse.setUsecase(use.getUsecase());
                aoUse.setDestination(use.getDestination());
                return aoUse;
            }).toList());
            AddressDO address = found.get().getAddress();
            rsp.setAddress(address.getAddress());
            rsp.setUser(address.getUserId());
            if(!appId.equals(address.getAppId()))
                throw new ApiExceptionBuilder(HttpStatus.UNAUTHORIZED)
                        .help("Check your auth token and transaction id")
                        .build();

            byte[] raw = blockService.fetch(found.get().getBlock().getSrc(), transaction);
            List<byte[]> decoded = Decode.bytes(raw);
            int version = Decode.bigInt(decoded.get(0)).intValue();
            if(version == 2) {
                rsp.setTimestamp(Decode.dateTime(decoded.get(2)));
                AOSignature userSig = new AOSignature();
                userSig.setSignature(B64.encode(decoded.get(4)));
                userSig.setPubkey("https://bucket.storage.l0.mytiki.com/" +
                        address.getAppId() + "/" +
                        address.getAddress() + "/public.key");
                rsp.setUserSignature(userSig);

                String appSignature = B64.encode(decoded.get(5));
                if(!appSignature.isBlank()){
                    AOSignature appSig = new AOSignature();
                    appSig.setSignature(appSignature);
                    appSig.setPubkey("https://registry.l0.mytiki.com/api/latest/id/" +
                            address.getUserId() + "/pubkey");
                    rsp.setAppSignature(appSig);
                }

                List<byte[]> contents = Decode.bytes(decoded.get(6));
                int schema = Decode.bigInt(contents.get(0)).intValue();
                if(schema == 3){
                    rsp.setTerms(Decode.utf8(contents.get(2)));
                    rsp.setDescription(Decode.utf8(contents.get(3)));
                }
            }
        }
        return rsp;
    }
}

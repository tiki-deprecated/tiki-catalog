/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.title;

import com.mytiki.l0_index.features.latest.address.AddressDO;
import com.mytiki.l0_index.features.latest.address.AddressService;
import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.features.latest.index.IndexAOReqTitle;
import com.mytiki.l0_index.features.latest.tag.TagDO;
import com.mytiki.l0_index.features.latest.tag.TagService;
import com.mytiki.l0_index.utilities.AOSignature;
import com.mytiki.l0_index.utilities.B64;
import com.mytiki.l0_index.utilities.Decode;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.codec.Utf8;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public class TitleService {
    private final TitleRepository repository;
    private final TagService tagService;
    private final AddressService addressService;
    private final BlockService blockService;

    public TitleService(
            TitleRepository repository,
            TagService tagService,
            AddressService addressService,
            BlockService blockService) {
        this.repository = repository;
        this.tagService = tagService;
        this.addressService = addressService;
        this.blockService = blockService;
    }

    @Transactional
    public TitleDO insert(IndexAOReqTitle req, String appId, BlockDO block){
        Optional<TitleDO> found = repository.getByTransaction(req.getTransaction());
        if(found.isEmpty()) {
            TitleDO title = new TitleDO();
            title.setTransaction(req.getTransaction());
            title.setPtr(req.getPtr());
            title.setTags(tagService.insert(req.getTags()));
            title.setBlock(block);
            title.setAddress(addressService.insert(req.getAddress(), appId));
            title.setCreated(ZonedDateTime.now());
            return repository.save(title);
        }else
            return found.get();
    }

    public TitleDO getByTransaction(String transaction){
        Optional<TitleDO> found = repository.getByTransaction(transaction);
        return found.orElse(null);
    }

    @Transactional
    public TitleAORsp fetch(String transaction){
        TitleAORsp rsp = new TitleAORsp();
        Optional<TitleDO> found = repository.getByTransaction(transaction);
        if(found.isPresent()){
            rsp.setId(transaction);
            rsp.setPtr(found.get().getPtr());
            rsp.setTags(found.get().getTags().stream().map(TagDO::getValue).toList());
            AddressDO address = found.get().getAddress();
            rsp.setAddress(address.getAddress());
            rsp.setUser(address.getUserId());

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
                if(schema == 2){
                    rsp.setOrigin(Utf8.decode(contents.get(2)));
                    String desc = Utf8.decode(contents.get(3));
                    if(!desc.isBlank()) rsp.setDescription(desc);
                }
            }
        }
        return rsp;
    }
}

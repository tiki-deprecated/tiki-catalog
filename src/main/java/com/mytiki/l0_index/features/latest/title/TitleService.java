/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.title;

import com.mytiki.l0_index.features.latest.address.AddressService;
import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.index.IndexAOTitle;
import com.mytiki.l0_index.features.latest.tag.TagService;
import jakarta.transaction.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

public class TitleService {
    private final TitleRepository repository;
    private final TagService tagService;
    private final AddressService addressService;

    public TitleService(
            TitleRepository repository,
            TagService tagService,
            AddressService addressService) {
        this.repository = repository;
        this.tagService = tagService;
        this.addressService = addressService;
    }

    @Transactional
    public TitleDO insert(IndexAOTitle req, String appId, BlockDO block){
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
}

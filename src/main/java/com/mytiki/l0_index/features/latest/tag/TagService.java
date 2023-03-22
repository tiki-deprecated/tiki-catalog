/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.tag;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TagService {
    private final TagRepository repository;

    public TagService(TagRepository repository) {
        this.repository = repository;
    }

    public List<TagDO> insert(List<String> tags){
        List<TagDO> existing = repository.getAllByValueIn(tags);
        List<String> existingVals = existing.stream().map(TagDO::getValue).toList();
        ZonedDateTime now = ZonedDateTime.now();
        Set<TagDO> toCreate = tags
                .stream()
                .filter(val -> !existingVals.contains(val))
                .map(val ->  {
                    TagDO tag = new TagDO();
                    tag.setValue(val);
                    tag.setCreated(now);
                    return tag;
                })
                .collect(Collectors.toSet());
        List<TagDO> created = repository.saveAll(toCreate);
        created.addAll(existing);
        return created;
    }
}

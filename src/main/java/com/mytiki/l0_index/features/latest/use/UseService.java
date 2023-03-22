/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.use;

import com.mytiki.l0_index.utilities.AOUse;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class UseService {
    private final UseRepository repository;

    public UseService(UseRepository repository) {
        this.repository = repository;
    }

    public List<UseDO> insert(List<AOUse> uses){
        Set<String> usecases = new HashSet<>();
        Map<String, AOUse> composites = new HashMap<>();
        uses.forEach(use -> {
            usecases.add(use.getUsecase());
            composites.put(use.getUsecase() + ":" + use.getDestination(), use);
        });

        List<UseDO> existing = repository.getAllByUsecaseIn(usecases)
                .stream()
                .filter(use -> composites.containsKey(use.getUsecase() + ":" + use.getDestination()))
                .toList();
        Set<String> existingComposites = new HashSet<>();
        existing.forEach(use -> existingComposites.add(use.getUsecase() + ":" + use.getDestination()));

        ZonedDateTime now = ZonedDateTime.now();
        Set<UseDO> toCreate = composites.entrySet()
                .stream()
                .filter(entry -> !existingComposites.contains(entry.getKey()))
                .map(entry -> {
                  UseDO use = new UseDO();
                  use.setUsecase(entry.getValue().getUsecase());
                  use.setDestination(entry.getValue().getDestination());
                  use.setCreated(now);
                  return use;
                })
                .collect(Collectors.toSet());

        List<UseDO> created = repository.saveAll(toCreate);
        created.addAll(existing);
        return created;
    }
}

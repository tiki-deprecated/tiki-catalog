/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.count;

import java.util.Optional;

public class CountService {
    private final CountRepository repository;

    public CountService(CountRepository repository) {
        this.repository = repository;
    }

    public Float get(String table){
        Optional<CountDO> count = repository.getByRelname(table);
        if(count.isEmpty() || count.get().getReltuples() == -1)
            return null;
        else
            return count.get().getReltuples();
    }
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.block;

import org.springframework.data.jpa.repository.JpaRepository;

import java.net.URL;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<BlockDO, Long> {
    Optional<BlockDO> findBySrc(URL src);
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.block;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<BlockDO, Long> {
    Page<BlockDO> findAllByAddressAppIdAndAddressAddress(String appId, byte[] address, Pageable pageable);
    Optional<BlockDO> findByHashAndAddressAppIdAndAddressAddress(byte[] hash, String appId, byte[] address);
    List<BlockDO> findByHash(byte[] hash);
}

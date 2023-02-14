/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.address;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressDO, Long> {
    Optional<AddressDO> findByAppIdAndAddress(String appId, byte[] address);
    Page<AddressDO> findAllByAppId(String appId, Pageable pageable);
}

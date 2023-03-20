/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.license;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LicenseRepository extends JpaRepository<LicenseDO, Long> {
    Optional<LicenseDO> getByTransaction(String txn);

}

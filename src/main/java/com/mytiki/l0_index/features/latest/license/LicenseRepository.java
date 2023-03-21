/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.license;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LicenseRepository extends JpaRepository<LicenseDO, Long>, LicenseRepositorySearch {
    Optional<LicenseDO> getByTransaction(String txn);

    @Modifying
    @Query("update LicenseDO l set l.latest = false where l.title.id = :title")
    void clearLatest(@Param("title") Long title);
}

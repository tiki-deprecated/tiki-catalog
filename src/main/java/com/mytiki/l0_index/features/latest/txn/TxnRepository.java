/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.txn;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TxnRepository extends JpaRepository<TxnDO, Long> {
    Optional<TxnDO> findByHashAndBlockHashAndBlockAddressAppIdAndBlockAddressAddress(
            byte[] txnHash, byte[] blockHash, String appId, byte[] address);
    @Transactional
    void deleteByHash(byte[] hash);
    List<TxnDO> findByHash(byte[] hash);
}

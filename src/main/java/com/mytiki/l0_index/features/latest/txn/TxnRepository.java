/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.txn;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TxnRepository extends JpaRepository<TxnDO, Long> {
    Optional<TxnDO> findByHashAndBlockHashAndBlockAddressAidAndBlockAddressAddress(
            byte[] txnHash, byte[] blockHash, String aid, byte[] address);
    @Transactional
    void deleteByHash(byte[] hash);
}

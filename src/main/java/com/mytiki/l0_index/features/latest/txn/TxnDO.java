/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.txn;

import com.mytiki.l0_index.features.latest.block.BlockDO;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "txn")
public class TxnDO {
    private Long id;
    private byte[] hash;
    private BlockDO block;
    private ZonedDateTime created;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "txn_hash")
    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "block_id")
    public BlockDO getBlock() {
        return block;
    }

    public void setBlock(BlockDO block) {
        this.block = block;
    }

    @Column(name = "created_utc")
    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }
}

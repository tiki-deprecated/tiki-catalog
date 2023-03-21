/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.license;

import com.mytiki.l0_index.features.latest.address.AddressDO;
import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.title.TitleDO;
import com.mytiki.l0_index.features.latest.use.UseDO;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "license")
public class LicenseDO implements Serializable {
    private Long id;
    private String transaction;
    private BlockDO block;
    private AddressDO address;
    private TitleDO title;
    private ZonedDateTime expiry;
    private ZonedDateTime created;
    private List<UseDO> uses;
    private boolean isLatest;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "license_id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "txn_hash")
    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_id")
    public BlockDO getBlock() {
        return block;
    }

    public void setBlock(BlockDO block) {
        this.block = block;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    public AddressDO getAddress() {
        return address;
    }

    public void setAddress(AddressDO address) {
        this.address = address;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "title_id")
    public TitleDO getTitle() {
        return title;
    }

    public void setTitle(TitleDO title) {
        this.title = title;
    }

    @Column(name = "expiry")
    public ZonedDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(ZonedDateTime expiry) {
        this.expiry = expiry;
    }

    @Column(name = "created")
    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "license_use",
            joinColumns = @JoinColumn(name = "license_id"),
            inverseJoinColumns = @JoinColumn(name = "use_id"))
    public List<UseDO> getUses() {
        return uses;
    }

    public void setUses(List<UseDO> uses) {
        this.uses = uses;
    }

    @Column(name = "is_latest")
    public boolean isLatest() {
        return isLatest;
    }

    public void setLatest(boolean latest) {
        isLatest = latest;
    }
}

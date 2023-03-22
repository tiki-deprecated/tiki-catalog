/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.title;

import com.mytiki.l0_index.features.latest.address.AddressDO;
import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.tag.TagDO;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "title")
public class TitleDO implements Serializable {
    private Long id;
    private String transaction;
    private String ptr;
    private BlockDO block;
    private AddressDO address;
    private ZonedDateTime created;
    private List<TagDO> tags;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "title_id")
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

    @Column(name = "ptr")
    public String getPtr() {
        return ptr;
    }

    public void setPtr(String ptr) {
        this.ptr = ptr;
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

    @Column(name = "created")
    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "title_tag",
            joinColumns = @JoinColumn(name = "title_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    public List<TagDO> getTags() {
        return tags;
    }

    public void setTags(List<TagDO> tags) {
        this.tags = tags;
    }
}

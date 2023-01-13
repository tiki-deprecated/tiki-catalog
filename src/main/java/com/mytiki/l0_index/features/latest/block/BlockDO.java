/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.block;

import com.mytiki.l0_index.features.latest.address.AddressDO;
import jakarta.persistence.*;

import java.io.Serializable;
import java.net.URL;
import java.time.ZonedDateTime;

@Entity
@Table(name = "block")
public class BlockDO implements Serializable {
    private Long id;
    private byte[] hash;
    private AddressDO address;
    private URL src;
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

    @Column(name = "block_hash")
    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    public AddressDO getAddress() {
        return address;
    }

    public void setAddress(AddressDO address) {
        this.address = address;
    }

    @Column(name = "src_url")
    public URL getSrc() {
        return src;
    }

    public void setSrc(URL src) {
        this.src = src;
    }

    @Column(name = "created_utc")
    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }
}

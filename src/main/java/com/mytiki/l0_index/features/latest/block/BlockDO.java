/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.block;

import jakarta.persistence.*;

import java.io.Serializable;
import java.net.URL;
import java.time.ZonedDateTime;

@Entity
@Table(name = "block")
public class BlockDO implements Serializable {
    private Long id;
    private String hash;
    private URL src;
    private ZonedDateTime created;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "block_hash")
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Column(name = "src_url")
    public URL getSrc() {
        return src;
    }

    public void setSrc(URL src) {
        this.src = src;
    }

    @Column(name = "created")
    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }
}

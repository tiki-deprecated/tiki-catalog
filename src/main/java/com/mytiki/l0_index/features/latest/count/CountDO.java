/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.count;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "pg_class", schema = "pg_catalog")
public class CountDO implements Serializable {
    private Long oid;
    private Float reltuples;
    private String relname;

    @Id
    @Column(name = "oid")
    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    @Column(name = "reltuples")
    public Float getReltuples() {
        return reltuples;
    }

    public void setReltuples(Float reltuples) {
        this.reltuples = reltuples;
    }

    @Column(name = "relname")
    public String getRelname() {
        return relname;
    }

    public void setRelname(String relname) {
        this.relname = relname;
    }
}

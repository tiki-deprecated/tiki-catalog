/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.block;

import java.util.List;

public class BlockPageAO {
    private int page;
    private int totalPages;
    private long totalHashes;
    private List<String> hashes;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalHashes() {
        return totalHashes;
    }

    public void setTotalHashes(long totalHashes) {
        this.totalHashes = totalHashes;
    }

    public List<String> getHashes() {
        return hashes;
    }

    public void setHashes(List<String> hashes) {
        this.hashes = hashes;
    }
}

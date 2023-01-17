/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.address;

import java.util.List;

public class AddressPageAO {

    private int page;
    private int totalPages;
    private long totalAddresses;
    private List<String> addresses;

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

    public long getTotalAddresses() {
        return totalAddresses;
    }

    public void setTotalAddresses(long totalAddresses) {
        this.totalAddresses = totalAddresses;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }
}

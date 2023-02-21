/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.search;

import com.mytiki.l0_index.features.latest.address.AddressDO;
import com.mytiki.l0_index.features.latest.address.AddressService;
import com.mytiki.l0_index.features.latest.app.AppAO;
import com.mytiki.l0_index.features.latest.app.AppService;
import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.features.latest.txn.TxnDO;
import com.mytiki.l0_index.features.latest.txn.TxnService;
import com.mytiki.l0_index.utilities.B64;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

public class SearchService {

    private final AppService appService;
    private final AddressService addressService;
    private final BlockService blockService;
    private final TxnService txnService;

    public SearchService(
            AppService appService,
            AddressService addressService,
            BlockService blockService,
            TxnService txnService) {
        this.appService = appService;
        this.addressService = addressService;
        this.blockService = blockService;
        this.txnService = txnService;
    }

    @Transactional
    public List<SearchAO> search(String id){
       AppAO app = appService.getApp(id, 0, 1);
       List<AddressDO> addresses = addressService.findByAddress(id);
       List<TxnDO> txns = txnService.findByHash(id);
       List<BlockDO> blocks = blockService.findByHash(id);

       List<SearchAO> res = new ArrayList<>();
       if(app.getAddresses().getTotalAddresses() > 0){
           SearchAO appRes = new SearchAO();
           appRes.setAppId(app.getAppId());
           res.add(appRes);
       }

       addresses.forEach(addr -> {
           SearchAO addrRes = new SearchAO();
           addrRes.setAddress(B64.encode(addr.getAddress()));
           addrRes.setAppId(addr.getAppId());
           res.add(addrRes);
       });

       blocks.forEach(block -> {
            SearchAO blockRes = new SearchAO();
            blockRes.setBlockHash(B64.encode(block.getHash()));
            blockRes.setAddress(B64.encode(block.getAddress().getAddress()));
            blockRes.setAppId(block.getAddress().getAppId());
            res.add(blockRes);
       });

        txns.forEach(txn -> {
            SearchAO txnRes = new SearchAO();
            txnRes.setTxnHash(B64.encode(txn.getHash()));
            txnRes.setBlockHash(B64.encode(txn.getBlock().getHash()));
            txnRes.setAddress(B64.encode(txn.getBlock().getAddress().getAddress()));
            txnRes.setAppId(txn.getBlock().getAddress().getAppId());
            res.add(txnRes);
        });

        return res;
    }
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.block;

import com.mytiki.l0_index.utilities.B64;
import com.mytiki.l0_index.utilities.Decode;
import com.mytiki.l0_index.utilities.Sha256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlockService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final BlockRepository repository;
    private final RestTemplate client;

    public BlockService(BlockRepository repository, RestTemplate client) {
        this.repository = repository;
        this.client = client;
    }

    public BlockPageAO page(String apiId, byte[] address, int num, int size){
        Page<BlockDO> page = repository.findAllByAddressAidAndAddressAddress(apiId, address, PageRequest.of(num, size));
        BlockPageAO rsp = new BlockPageAO();
        rsp.setPage(page.getNumber());
        rsp.setTotalPages(page.getTotalPages());
        rsp.setTotalHashes(page.getTotalElements());
        rsp.setHashes(page.getContent().stream().map(block -> B64.encode(block.getHash())).toList());
        return rsp;
    }

    public BlockAO getBlock(String apiId, String address, String hash){
        BlockAO rsp = new BlockAO();
        rsp.setApiId(apiId);
        rsp.setAddress(address);
        rsp.setHash(hash);
        byte[] hashBytes = B64.decode(hash);
        byte[] addressBytes = B64.decode(address);
        Optional<BlockDO> found = repository.findByHashAndAddressAidAndAddressAddress(hashBytes, apiId, addressBytes);
        return found.map(blockDO -> fetchAndMerge(rsp, blockDO.getSrc())).orElse(rsp);
    }

    private BlockAO fetchAndMerge(BlockAO block, URL src){
        block.setUrl(src.toString());
        List<byte[]> binary = fetch(src);
        if(!binary.isEmpty()){
            block.setSignature(B64.encode(binary.get(0)));
            List<byte[]> body = Decode.bytes(binary.get(1));
            block.setVersion(Decode.bigInt(body.get(0)).intValue());
            block.setTimestamp(Decode.dateTime(body.get(1)));
            block.setPrevious(B64.encode(body.get(2)));
            block.setTransactionRoot(B64.encode(body.get(3)));
            int txnCount = Decode.bigInt(body.get(4)).intValue();
            List<String> txns = new ArrayList<>(txnCount);
            for (int i = 0; i < txnCount; i++)
                txns.add(B64.encode(Sha256.hash(body.get(5 + i))));
            block.setTransactions(txns);
        }
        return block;
    }

    public List<byte[]> fetch(URL src){
        try {
            ResponseEntity<byte[]> response = client.getForEntity(src.toURI(), byte[].class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null)
                return Decode.chars(Utf8.decode(response.getBody()).toCharArray());
            else return List.of();
        } catch (URISyntaxException e) {
            logger.error("src_url is an invalid URI", e);
            return List.of();
        }
    }
}

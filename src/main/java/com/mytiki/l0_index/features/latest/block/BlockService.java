/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.block;

import com.mytiki.l0_index.utilities.B64;
import com.mytiki.l0_index.utilities.Decode;
import com.mytiki.l0_index.utilities.Sha256;
import com.mytiki.spring_rest_api.ApiExceptionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZonedDateTime;
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

    public BlockDO insert(String hash, String src){
        try {
            URL srcUrl = new URL(src);
            Optional<BlockDO> found = repository.findBySrc(srcUrl);
            if(found.isEmpty()) {
                BlockDO block = new BlockDO();
                block.setHash(hash);
                block.setSrc(srcUrl);
                block.setCreated(ZonedDateTime.now());
                return repository.save(block);
            }else
                return found.get();
        } catch (MalformedURLException e) {
            throw new ApiExceptionBuilder(HttpStatus.BAD_REQUEST)
                    .message("Malformed url")
                    .properties("src", src)
                    .build();
        }
    }

    public byte[] fetch(URL src, String transaction){
        try {
            ResponseEntity<byte[]> response = client.getForEntity(src.toURI(), byte[].class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<byte[]> decoded = Decode.bytes(response.getBody());
                List<byte[]> block = Decode.bytes(decoded.get(1));
                int txnCount = Decode.bigInt(block.get(4)).intValue();
                for (int i = 0; i < txnCount; i++) {
                    String hash = B64.encode(Sha256.hash(block.get(5 + i)));
                    if(hash.equals(transaction))
                        return block.get(5 + i);
                }
            }
        } catch (URISyntaxException | ResourceAccessException e) {
            logger.error("src_url is an invalid URI", e);
        }
        return null;
    }
}

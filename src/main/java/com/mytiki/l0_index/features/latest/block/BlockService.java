/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.block;

import com.mytiki.spring_rest_api.ApiExceptionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
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

//    public List<byte[]> fetch(URL src){
//        try {
//            ResponseEntity<byte[]> response = client.getForEntity(src.toURI(), byte[].class);
//            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null)
//                return Decode.bytes(response.getBody());
//            else return List.of();
//        } catch (URISyntaxException | ResourceAccessException e) {
//            logger.error("src_url is an invalid URI", e);
//            return List.of();
//        }
//    }

//    private BlockAO fetchAndMerge(BlockAO block, URL src){
//        block.setUrl(src.toString());
//        List<byte[]> binary = fetch(src);
//        if(!binary.isEmpty()){
//            block.setSignature(B64.encode(binary.get(0)));
//            List<byte[]> body = Decode.bytes(binary.get(1));
//            block.setVersion(Decode.bigInt(body.get(0)).intValue());
//            block.setTimestamp(Decode.dateTime(body.get(1)));
//            block.setPrevious(B64.encode(body.get(2)));
//            block.setTransactionRoot(B64.encode(body.get(3)));
//            int txnCount = Decode.bigInt(body.get(4)).intValue();
//            List<String> txns = new ArrayList<>(txnCount);
//            for (int i = 0; i < txnCount; i++)
//                txns.add(B64.encode(Sha256.hash(body.get(5 + i))));
//            block.setTransactions(txns);
//        }
//        return block;
//    }
}

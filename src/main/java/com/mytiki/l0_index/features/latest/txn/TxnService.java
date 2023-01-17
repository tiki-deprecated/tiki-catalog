/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.txn;

import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.utilities.B64;
import com.mytiki.l0_index.utilities.Decode;
import com.mytiki.l0_index.utilities.Sha256;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TxnService {

    private final TxnRepository repository;
    private final BlockService blockService;

    public TxnService(TxnRepository repository, BlockService blockService) {
        this.repository = repository;
        this.blockService = blockService;
    }

    public TxnAO getTransaction(String apiId, String address, String blockHash, String txnHash){
        TxnAO rsp = new TxnAO();
        rsp.setApiId(apiId);
        rsp.setAddress(address);
        rsp.setBlock(blockHash);
        rsp.setHash(txnHash);

        byte[] addressBytes = B64.decode(address);
        byte[] blockHashBytes = B64.decode(blockHash);
        byte[] txnHashBytes = B64.decode(txnHash);

        Optional<TxnDO> found = repository.findByHashAndBlockHashAndBlockAddressAidAndBlockAddressAddress(
                txnHashBytes, blockHashBytes, apiId, addressBytes);

        if(found.isPresent()){
            rsp.setUrl(found.get().getBlock().getSrc().toString());
            List<byte[]> binary = fetch(found.get().getBlock().getSrc(), B64.decode(txnHash));
            if(!binary.isEmpty()) {
                rsp.setVersion(Decode.bigInt(binary.get(0)).intValue());
                rsp.setTimestamp(Decode.dateTime(binary.get(2)));
                rsp.setAssetRef(B64.encode(binary.get(3)));
                rsp.setSignature(B64.encode(binary.get(4)));

                List<byte[]> contentBytes = Decode.bytes(binary.get(5));
                TxnContentSchema schema = TxnContentSchema.fromId(Decode.bigInt(contentBytes.get(0)).intValue());
                if(schema == TxnContentSchema.UNKNOWN) schema = TxnContentSchema.CONSENT;

                rsp.setContentSchema(schema.getName());
                TxnAOContents contents = resolveContents(schema, contentBytes);
                contents.setRaw(B64.encode(binary.get(5)));
                rsp.setContents(contents);
            }
        }
        return rsp;
    }

    public List<byte[]> fetch(URL src, byte[] txnHash){
        List<byte[]> binary = blockService.fetch(src);
        if(!binary.isEmpty()){
            List<byte[]> body = Decode.bytes(binary.get(1));
            int txnCount = Decode.bigInt(body.get(4)).intValue();
            for (int i = 0; i < txnCount; i++) {
                byte[] txn = body.get(5 + i);
                if(Arrays.equals(txnHash, Sha256.hash(txn)))
                    return Decode.bytes(txn);
            }
        }
        return List.of();
    }

    private TxnAOContents resolveContents(TxnContentSchema schema, List<byte[]> bytes){
        switch (schema) {
            case CONSENT -> {
                TxnAOConsent consent = new TxnAOConsent();
                consent.setOwnershipId(B64.encode(bytes.get(0)));
                consent.setDestination(Decode.utf8(bytes.get(1)));
                consent.setAbout(Decode.utf8(bytes.get(2)));
                consent.setReward(Decode.utf8(bytes.get(3)));
                consent.setExpiry(Decode.dateTime(bytes.get(4)));
                return consent;
            }
            case OWNERSHIP -> {
                TxnAOOwnership ownership = new TxnAOOwnership();
                ownership.setSource(Decode.utf8(bytes.get(1)));
                ownership.setType(Decode.utf8(bytes.get(2)));
                ownership.setOrigin(Decode.utf8(bytes.get(3)));
                ownership.setAbout(Decode.utf8(bytes.get(4)));
                ownership.setContains(Decode.utf8(bytes.get(5)));
                return ownership;
            }
            default -> {
                return new TxnAORaw();
            }
        }
    }

    public TxnDO create(byte[] hash, BlockDO block){
        TxnDO req = new TxnDO();
        req.setHash(hash);
        req.setBlock(block);
        req.setCreated(ZonedDateTime.now());
        return repository.save(req);
    }
}

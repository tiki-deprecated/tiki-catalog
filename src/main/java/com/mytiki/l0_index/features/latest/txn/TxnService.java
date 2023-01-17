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

    public TxnAO<?> getTransaction(String apiId, String address, String blockHash, String txnHash){
        TxnAO<?> txn;
        byte[] addressBytes = B64.decode(address);
        byte[] blockHashBytes = B64.decode(blockHash);
        byte[] txnHashBytes = B64.decode(txnHash);

        Optional<TxnDO> found = repository.findByHashAndBlockHashAndBlockAddressAidAndBlockAddressAddress(
                txnHashBytes, blockHashBytes, apiId, addressBytes);

        if(found.isPresent()){
            List<byte[]> binary = fetch(found.get().getBlock().getSrc(), B64.decode(txnHash));
            if(!binary.isEmpty()) {
                txn = resolveContents(binary.get(5));
                txn.setVersion(Decode.bigInt(binary.get(0)).intValue());
                txn.setTimestamp(Decode.dateTime(binary.get(2)));
                txn.setAssetRef(B64.encode(binary.get(3)));
                txn.setSignature(B64.encode(binary.get(4)));
            }else
                txn = new TxnAO<>();
            txn.setUrl(found.get().getBlock().getSrc().toString());
        }else
            txn = new TxnAO<>();

        txn.setAddress(address);
        txn.setApiId(apiId);
        txn.setBlock(blockHash);
        txn.setHash(txnHash);
        return txn;
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

    public TxnAO<?> resolveContents(byte[] raw){
        List<byte[]> decoded = Decode.bytes(raw);
        TxnContentSchema schema = TxnContentSchema.fromId(Decode.bigInt(decoded.get(0)).intValue());

        if(schema == TxnContentSchema.UNKNOWN) schema = TxnContentSchema.CONSENT; //Temporary fix for bad block serialization.

        switch (schema) {
            case CONSENT -> {
                TxnAOContentsConsent consent = new TxnAOContentsConsent();
                consent.setOwnershipId(B64.encode(decoded.get(0)));
                consent.setDestination(Decode.utf8(decoded.get(1)));
                consent.setAbout(Decode.utf8(decoded.get(2)));
                consent.setReward(Decode.utf8(decoded.get(3)));
                consent.setExpiry(Decode.dateTime(decoded.get(4)));
                consent.setRaw(B64.encode(raw));
                TxnAO<TxnAOContentsConsent> txnConsent = new TxnAO<>();
                txnConsent.setContentSchema(TxnContentSchema.CONSENT.getName());
                txnConsent.setContents(consent);
                return txnConsent;
            }
            case OWNERSHIP -> {
                TxnAOContentsOwnership ownership = new TxnAOContentsOwnership();
                ownership.setSource(Decode.utf8(decoded.get(1)));
                ownership.setType(Decode.utf8(decoded.get(2)));
                ownership.setOrigin(Decode.utf8(decoded.get(3)));
                ownership.setAbout(Decode.utf8(decoded.get(4)));
                ownership.setContains(Decode.utf8(decoded.get(5)));
                ownership.setRaw(B64.encode(raw));
                TxnAO<TxnAOContentsOwnership> txnOwnership = new TxnAO<>();
                txnOwnership.setContentSchema(TxnContentSchema.OWNERSHIP.getName());
                txnOwnership.setContents(ownership);
                return txnOwnership;
            }
        }

        TxnAO<TxnAOContentsRaw> txnContents = new TxnAO<>();
        TxnAOContentsRaw contents = new TxnAOContentsRaw();
        contents.setRaw(B64.encode(raw));
        txnContents.setContentSchema(TxnContentSchema.UNKNOWN.getName());
        txnContents.setContents(contents);
        return txnContents;
    }

    public TxnDO create(byte[] hash, BlockDO block){
        TxnDO req = new TxnDO();
        req.setHash(hash);
        req.setBlock(block);
        req.setCreated(ZonedDateTime.now());
        return repository.save(req);
    }
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import com.mytiki.l0_index.features.latest.address.AddressDO;
import com.mytiki.l0_index.features.latest.address.AddressRepository;
import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.block.BlockRepository;
import com.mytiki.l0_index.features.latest.search.SearchAO;
import com.mytiki.l0_index.features.latest.search.SearchService;
import com.mytiki.l0_index.features.latest.txn.TxnDO;
import com.mytiki.l0_index.features.latest.txn.TxnRepository;
import com.mytiki.l0_index.main.App;
import com.mytiki.l0_index.utilities.B64;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {App.class}
)
@ActiveProfiles(profiles = {"ci", "dev", "local"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SearchTest {

    @Autowired
    private TxnRepository txnRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private SearchService service;

    @Test
    public void Test_SearchApp_Success() {
        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();

        AddressDO testAddress = new AddressDO();
        testAddress.setAddress(B64.decode(address));
        testAddress.setAppId(appId);
        testAddress.setCreated(ZonedDateTime.now());
        addressRepository.save(testAddress);

        List<SearchAO> found = service.search(appId);
        assertEquals(1, found.size());
        assertEquals(appId, found.get(0).getAppId());
        assertNull(found.get(0).getAddress());
        assertNull(found.get(0).getBlockHash());
        assertNull(found.get(0).getTxnHash());
    }

    @Test
    public void Test_SearchAddress_Success() {
        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();

        AddressDO testAddress = new AddressDO();
        testAddress.setAddress(B64.decode(address));
        testAddress.setAppId(appId);
        testAddress.setCreated(ZonedDateTime.now());
        addressRepository.save(testAddress);

        List<SearchAO> found = service.search(address);
        assertEquals(1, found.size());
        assertEquals(appId, found.get(0).getAppId());
        assertEquals(address, found.get(0).getAddress());
        assertNull(found.get(0).getBlockHash());
        assertNull(found.get(0).getTxnHash());
    }

    @Test
    public void Test_SearchBlock_Success() throws MalformedURLException {
        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();
        String hash = UUID.randomUUID().toString();
        URL src = new URL("http://localhost:8080/mockedBlock");

        AddressDO testAddress = new AddressDO();
        testAddress.setAddress(B64.decode(address));
        testAddress.setAppId(appId);
        testAddress.setCreated(ZonedDateTime.now());
        addressRepository.save(testAddress);

        BlockDO testBlock = new BlockDO();
        testBlock.setAddress(testAddress);
        testBlock.setHash(B64.decode(hash));
        testBlock.setSrc(src);
        testBlock.setCreated(ZonedDateTime.now());
        blockRepository.save(testBlock);

        List<SearchAO> found = service.search(hash);
        assertEquals(1, found.size());
        assertEquals(appId, found.get(0).getAppId());
        assertEquals(address, found.get(0).getAddress());
        assertEquals(hash, found.get(0).getBlockHash());
        assertNull(found.get(0).getTxnHash());
    }

    @Test
    public void Test_SearchTxn_Success() throws MalformedURLException {
        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();
        String blockHash = UUID.randomUUID().toString();
        String txnHash = UUID.randomUUID().toString();
        URL src = new URL("http://localhost:8080/mockedBlock");

        AddressDO testAddress = new AddressDO();
        testAddress.setAddress(B64.decode(address));
        testAddress.setAppId(appId);
        testAddress.setCreated(ZonedDateTime.now());
        addressRepository.save(testAddress);

        BlockDO testBlock = new BlockDO();
        testBlock.setAddress(testAddress);
        testBlock.setHash(B64.decode(blockHash));
        testBlock.setSrc(src);
        testBlock.setCreated(ZonedDateTime.now());
        blockRepository.save(testBlock);

        TxnDO testTxn = new TxnDO();
        testTxn.setBlock(testBlock);
        testTxn.setCreated(ZonedDateTime.now());
        testTxn.setHash(B64.decode(txnHash));
        txnRepository.save(testTxn);

        List<SearchAO> found = service.search(txnHash);
        assertEquals(1, found.size());
        assertEquals(appId, found.get(0).getAppId());
        assertEquals(address, found.get(0).getAddress());
        assertEquals(blockHash, found.get(0).getBlockHash());
        assertEquals(txnHash, found.get(0).getTxnHash());
    }

    @Test
    public void Test_SearchNone_Success() {
        List<SearchAO> found = service.search(UUID.randomUUID().toString());
        assertEquals(0, found.size());
    }
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import com.mytiki.l0_index.features.latest.address.AddressDO;
import com.mytiki.l0_index.features.latest.address.AddressRepository;
import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.block.BlockRepository;
import com.mytiki.l0_index.features.latest.report.ReportAO;
import com.mytiki.l0_index.features.latest.report.ReportService;
import com.mytiki.l0_index.features.latest.txn.TxnDO;
import com.mytiki.l0_index.features.latest.txn.TxnRepository;
import com.mytiki.l0_index.main.App;
import com.mytiki.l0_index.utilities.B64;
import com.mytiki.spring_rest_api.ApiException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {App.class}
)
@ActiveProfiles(profiles = {"ci", "dev", "local"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReportTest {

    @Autowired
    private ReportService service;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private TxnRepository txnRepository;

    @Test
    public void Test_Report_Success() {
        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();
        String blockHash = UUID.randomUUID().toString();
        String txnHash1 = UUID.randomUUID().toString();
        String txnHash2 = UUID.randomUUID().toString();
        String src = "http://localhost:8080/mockedBlock";

        ReportAO req = new ReportAO(appId, address, blockHash, src,  List.of(txnHash1, txnHash2));
        service.report(req);

        Optional<AddressDO> foundAddress = addressRepository.findByAidAndAddress(appId, B64.decode(address));
        Optional<BlockDO> foundBlock = blockRepository.findByHashAndAddressAidAndAddressAddress(
                B64.decode(blockHash), appId, B64.decode(address));
        Optional<TxnDO> foundTxn1 = txnRepository.findByHashAndBlockHashAndBlockAddressAidAndBlockAddressAddress(
                B64.decode(txnHash1), B64.decode(blockHash), appId, B64.decode(address));
        Optional<TxnDO> foundTxn2 = txnRepository.findByHashAndBlockHashAndBlockAddressAidAndBlockAddressAddress(
                B64.decode(txnHash2), B64.decode(blockHash), appId, B64.decode(address));

        assertTrue(foundAddress.isPresent());
        assertEquals(address, B64.encode(foundAddress.get().getAddress()));
        assertEquals(appId, foundAddress.get().getAid());
        assertNotNull(foundAddress.get().getCreated());

        assertTrue(foundBlock.isPresent());
        assertEquals(blockHash, B64.encode(foundBlock.get().getHash()));
        assertEquals(src, foundBlock.get().getSrc().toString());
        assertNotNull(foundBlock.get().getCreated());

        assertTrue(foundTxn1.isPresent());
        assertEquals(txnHash1, B64.encode(foundTxn1.get().getHash()));
        assertNotNull(foundTxn1.get().getCreated());

        assertTrue(foundTxn2.isPresent());
        assertEquals(txnHash2, B64.encode(foundTxn2.get().getHash()));
        assertNotNull(foundTxn2.get().getCreated());
    }

    @Test
    public void Test_Report_DuplicateAddressApiId_Success() {
        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();
        String blockHash = UUID.randomUUID().toString();
        String txnHash1 = UUID.randomUUID().toString();
        String txnHash2 = UUID.randomUUID().toString();
        String src = "http://localhost:8080/mockedBlock";

        AddressDO dupe = new AddressDO();
        dupe.setAddress(B64.decode(address));
        dupe.setAid(appId);
        dupe.setCreated(ZonedDateTime.now());
        addressRepository.save(dupe);

        ReportAO req = new ReportAO(appId, address, blockHash, src,  List.of(txnHash1, txnHash2));
        service.report(req);

        Optional<AddressDO> foundAddress = addressRepository.findByAidAndAddress(appId, B64.decode(address));
        Optional<BlockDO> foundBlock = blockRepository.findByHashAndAddressAidAndAddressAddress(
                B64.decode(blockHash), appId, B64.decode(address));
        Optional<TxnDO> foundTxn1 = txnRepository.findByHashAndBlockHashAndBlockAddressAidAndBlockAddressAddress(
                B64.decode(txnHash1), B64.decode(blockHash), appId, B64.decode(address));
        Optional<TxnDO> foundTxn2 = txnRepository.findByHashAndBlockHashAndBlockAddressAidAndBlockAddressAddress(
                B64.decode(txnHash2), B64.decode(blockHash), appId, B64.decode(address));

        assertTrue(foundAddress.isPresent());
        assertEquals(address, B64.encode(foundAddress.get().getAddress()));
        assertEquals(appId, foundAddress.get().getAid());
        assertNotNull(foundAddress.get().getCreated());

        assertTrue(foundBlock.isPresent());
        assertEquals(blockHash, B64.encode(foundBlock.get().getHash()));
        assertEquals(src, foundBlock.get().getSrc().toString());
        assertNotNull(foundBlock.get().getCreated());

        assertTrue(foundTxn1.isPresent());
        assertEquals(txnHash1, B64.encode(foundTxn1.get().getHash()));
        assertNotNull(foundTxn1.get().getCreated());

        assertTrue(foundTxn2.isPresent());
        assertEquals(txnHash2, B64.encode(foundTxn2.get().getHash()));
        assertNotNull(foundTxn2.get().getCreated());
    }

    @Test
    public void Test_Report_BadSrc_Failure() {
        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();
        String blockHash = UUID.randomUUID().toString();
        String txnHash1 = UUID.randomUUID().toString();
        String txnHash2 = UUID.randomUUID().toString();
        String src = "junk";

        ReportAO req = new ReportAO(appId, address, blockHash, src,  List.of(txnHash1, txnHash2));

        ApiException ex = assertThrows(ApiException.class, () -> service.report(req));
        assertEquals(ex.getHttpStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void Test_Report_NoTxn_Failure() {
        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();
        String blockHash = UUID.randomUUID().toString();
        String src = "http://localhost:8080/mockedBlock";

        ReportAO req = new ReportAO(appId, address, blockHash, src,  List.of());

        ApiException ex = assertThrows(ApiException.class, () -> service.report(req));
        assertEquals(ex.getHttpStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void Test_Report_DuplicateBlock_Failure() throws MalformedURLException {
        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();
        String blockHash = UUID.randomUUID().toString();
        String txnHash1 = UUID.randomUUID().toString();
        String txnHash2 = UUID.randomUUID().toString();
        String src = "http://localhost:8080/mockedBlock";

        AddressDO dupeAddress = new AddressDO();
        dupeAddress.setAddress(B64.decode(address));
        dupeAddress.setAid(appId);
        dupeAddress.setCreated(ZonedDateTime.now());
        addressRepository.save(dupeAddress);

        BlockDO dupeBlock = new BlockDO();
        dupeBlock.setAddress(dupeAddress);
        dupeBlock.setHash(B64.decode(blockHash));
        dupeBlock.setSrc(new URL(src));
        dupeBlock.setCreated(ZonedDateTime.now());
        blockRepository.save(dupeBlock);

        ReportAO req = new ReportAO(appId, address, blockHash, src,  List.of(txnHash1, txnHash2));
        ApiException ex = assertThrows(ApiException.class, () -> service.report(req));
        assertEquals(ex.getHttpStatus(), HttpStatus.BAD_REQUEST);
    }
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import com.mytiki.l0_index.features.latest.address.AddressDO;
import com.mytiki.l0_index.features.latest.address.AddressRepository;
import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.block.BlockRepository;
import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.features.latest.txn.*;
import com.mytiki.l0_index.main.App;
import com.mytiki.l0_index.utilities.B64;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {App.class}
)
@ActiveProfiles(profiles = {"ci", "dev", "local"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TxnTest {

    @Autowired
    private TxnRepository txnRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(testRestTemplate.getRestTemplate());
    }

    @Test
    public void Test_GetTxn_Ownership_Success() throws MalformedURLException, URISyntaxException {
        mockServer.expect(requestTo(new URI("http://localhost:8080/mockedBlock"))).andRespond(
                withStatus(HttpStatus.OK).body(MockedBlock.get()));

        BlockService blockService = new BlockService(blockRepository, testRestTemplate.getRestTemplate());
        TxnService txnService = new TxnService(txnRepository, blockService);

        String txnHash = "-TCPtpG11vITi0J3stisH834F4FCY65p1F5V4LtX75U";
        txnRepository.deleteByHash(B64.decode(txnHash));

        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();
        String blockHash = UUID.randomUUID().toString();
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

        TxnAO found = txnService.getTransaction(appId, address, blockHash, txnHash);

        assertEquals(appId, found.getAppId());
        assertEquals(address, found.getAddress());
        assertEquals(blockHash, found.getBlock());
        assertEquals(txnHash, found.getHash());
        assertEquals(src.toString(), found.getUrl());
        assertEquals(1, found.getVersion());
        assertEquals("AA", found.getAssetRef());
        assertEquals("2023-02-14T04:10:03Z", found.getTimestamp().toString());
        assertEquals("WdVIJ2BLcWfImPq2hEoH9xSGZHwsRjidvIj_yrkzyIz0_tfEjQqWYVKufLp5XdAktKNjQ6Krz30ATPLX62r9vFQiVov-aLeDgejh2ihMP8__0Vl6XS-k_49dw0ygf9YyI2tj9xXh5MUsgbjZO_9zNtF1OoR5n9PrEauRxoGpnd4-bGEl7h7K4o8iiCpMWZ0scH0l5PQw55WGEi-wczDslmE-XzO_qTZzTnmyOLjelY5wrFS2g_lEA07HCidAAA_x9yujW-DGOkxZMoP7AItq4oUUV0dkaqsG4PM8kZkgIHhpyJ3pQEK-sM-VjzJm518Kk2ul25ILgyeybsx-A6nbvg",
                found.getSignature());
        assertEquals(TxnContentSchema.OWNERSHIP.getName(), found.getContentSchema());

        TxnAOOwnership ownership = (TxnAOOwnership) found.getContents();
        assertEquals("eyJtZXNzYWdlIiA6ICJIZWxsbyBUaWtpISJ9", ownership.getSource());
        assertEquals("data_stream", ownership.getType());
        assertEquals("com.mytiki.tiki_sdk_example", ownership.getOrigin());
        assertEquals("[\"Test data\"]", ownership.getContains());
        assertEquals("AQEkZXlKdFpYTnpZV2RsSWlBNklDSklaV3hzYnlCVWFXdHBJU0o5C2RhdGFfc3RyZWFtG2NvbS5teXRpa2kudGlraV9zZGtfZXhhbXBsZQEADVsiVGVzdCBkYXRhIl0",
                found.getContents().getRaw());
    }

    @Test
    public void Test_GetTxn_Consent_Success() throws MalformedURLException, URISyntaxException {
        mockServer.expect(requestTo(new URI("http://localhost:8080/mockedBlock"))).andRespond(
                withStatus(HttpStatus.OK).body(MockedBlock.get()));

        BlockService blockService = new BlockService(blockRepository, testRestTemplate.getRestTemplate());
        TxnService txnService = new TxnService(txnRepository, blockService);

        String txnHash = "TfP2bf66oS0ysSGgqDA7JshI0QQBTmSds-2ngT2NisM";
        txnRepository.deleteByHash(B64.decode(txnHash));

        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();
        String blockHash = UUID.randomUUID().toString();
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

        TxnAO found = txnService.getTransaction(appId, address, blockHash, txnHash);

        assertEquals(appId, found.getAppId());
        assertEquals(address, found.getAddress());
        assertEquals(blockHash, found.getBlock());
        assertEquals(txnHash, found.getHash());
        assertEquals(src.toString(), found.getUrl());
        assertEquals(1, found.getVersion());
        assertEquals("AA", found.getAssetRef());
        assertEquals("2023-02-14T04:10:19Z", found.getTimestamp().toString());
        assertEquals("W-7kCbVhPOlBAM3T0kwl3K5_zlY9cuXuf6m-c7EmGZJNoz466kWgUVEaasGisVDYxkcpqBhV5bvspNpA-JKs3WyRYiJKluevR4G4L3QYgoNduWpZuilurjM0aANUbDS9XTdC1HdpfFT_VfkFsICKgFqROiIXkjjS4MSNwRxcb9ApWuUJEdhTFJRmq-jLXtInecluqsqI5NXEB76y0cG3RHQg3mE18pGXvD5N9M8lfsrwpbU8Iy5UrSAVaAAXZJgw2W1Wk5_p-yn4scSfRu4p7ZTWPjgZPlbIbAZOO2KuWrApDqXHJ2NElc7n941JBKL-bgunUA8SNm_iL5WeF_Kl8w",
                found.getSignature());
        assertEquals(TxnContentSchema.CONSENT.getName(), found.getContentSchema());

        TxnAOConsent consent = (TxnAOConsent) found.getContents();
        assertEquals("-TCPtpG11vITi0J3stisH834F4FCY65p1F5V4LtX75U", consent.getOwnershipId());
        assertEquals("![\"https://postman-echo.com/post\"]\b[\"POST\"]", consent.getDestination());
        assertNull(consent.getAbout());
        assertNull(consent.getReward());
        assertNull(consent.getExpiry());
        assertEquals("IPkwj7aRtdbyE4tCd7LYrB_N-BeBQmOuadReVeC7V--VKyFbImh0dHBzOi8vcG9zdG1hbi1lY2hvLmNvbS9wb3N0Il0IWyJQT1NUIl0BAAEAAQA",
                found.getContents().getRaw());
    }

    @Test
    public void Test_GetTxn_MissingTxn_Success() throws MalformedURLException, URISyntaxException {
        mockServer.expect(requestTo(new URI("http://localhost:8080/mockedBlock"))).andRespond(
                withStatus(HttpStatus.OK).body(MockedBlock.get()));

        BlockService blockService = new BlockService(blockRepository, testRestTemplate.getRestTemplate());
        TxnService txnService = new TxnService(txnRepository, blockService);

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

        TxnAO found = txnService.getTransaction(appId, address, blockHash, txnHash);

        assertEquals(appId, found.getAppId());
        assertEquals(address, found.getAddress());
        assertEquals(blockHash, found.getBlock());
        assertEquals(txnHash, found.getHash());
        assertEquals(src.toString(), found.getUrl());
    }

    @Test
    public void Test_GetTxn_InvalidSrc_Success() throws MalformedURLException, URISyntaxException {
        mockServer.expect(requestTo(new URI("http://localhost:8080/mockedBlock"))).andRespond(
                withStatus(HttpStatus.NOT_FOUND));

        BlockService blockService = new BlockService(blockRepository, testRestTemplate.getRestTemplate());
        TxnService txnService = new TxnService(txnRepository, blockService);

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

        TxnAO found = txnService.getTransaction(appId, address, blockHash, txnHash);

        assertEquals(appId, found.getAppId());
        assertEquals(address, found.getAddress());
        assertEquals(blockHash, found.getBlock());
        assertEquals(txnHash, found.getHash());
        assertEquals(src.toString(), found.getUrl());
    }

    @Test
    public void Test_GetTxn_NoTxn_Success() {
        BlockService blockService = new BlockService(blockRepository, testRestTemplate.getRestTemplate());
        TxnService txnService = new TxnService(txnRepository, blockService);

        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();
        String blockHash = UUID.randomUUID().toString();
        String txnHash = UUID.randomUUID().toString();

        TxnAO found = txnService.getTransaction(appId, address, blockHash, txnHash);

        assertEquals(appId, found.getAppId());
        assertEquals(address, found.getAddress());
        assertEquals(blockHash, found.getBlock());
        assertEquals(txnHash, found.getHash());
    }
}

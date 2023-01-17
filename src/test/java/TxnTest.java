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

        String txnHash = "9G88VZEb7eyJSS5L71NLRfKN1Ab0Y3Sj6b-1azllfNM";
        txnRepository.deleteByHash(B64.decode(txnHash));

        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();
        String blockHash = UUID.randomUUID().toString();
        URL src = new URL("http://localhost:8080/mockedBlock");

        AddressDO testAddress = new AddressDO();
        testAddress.setAddress(B64.decode(address));
        testAddress.setAid(appId);
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

        TxnAO<TxnAOContentsOwnership> found = (TxnAO<TxnAOContentsOwnership>) txnService.getTransaction(appId, address, blockHash, txnHash);

        assertEquals(appId, found.getApiId());
        assertEquals(address, found.getAddress());
        assertEquals(blockHash, found.getBlock());
        assertEquals(txnHash, found.getHash());
        assertEquals(src.toString(), found.getUrl());
        assertEquals(1, found.getVersion());
        assertEquals("AA", found.getAssetRef());
        assertEquals("2023-01-12T02:56:10Z", found.getTimestamp().toString());
        assertEquals("Ehlzv9uQp6FIy6QpwajErT-gKIbUPd0ycAWCUBZda252y6sK9xxOl25qQaRpKqerRjyE8a6QebVrU-qmdwbMvSbD93k-qRcTelk6rOuDuaWYma2IzMmi3bd74WyG5Gb_ObBdaPtdT7Dk3ITzuzdS-x88LuFvuQDwBFsLpkbuLiVsoc545ZiM1_UQ_G5YI_6IiuFHFU9f_4lKioLsr2yREcvisdPBWRpd5-5RKQUyB6nqYyly38n7mo9Djpo22VVkFoctDnVyA-CkWvNEpObTuYbExzTvE-SDVZqFWVZVRjR9ygexbAYOWFk9wVIZTborV4qvCmgbXZ-DdDDrdiL1Pg",
                found.getSignature());
        assertEquals(TxnContentSchema.OWNERSHIP.getName(), found.getContentSchema());
        assertEquals("user_92e71914-5766-417e-940e-c39492cdf3df", found.getContents().getSource());
        assertEquals("data_point", found.getContents().getType());
        assertEquals("com.mytiki.example_flutter_tos", found.getContents().getOrigin());
        assertEquals("You can put the Terms of Service the user agreed to here.", found.getContents().getAbout());
        assertEquals("[\"user_id\"]", found.getContents().getContains());
        assertEquals("AQEpdXNlcl85MmU3MTkxNC01NzY2LTQxN2UtOTQwZS1jMzk0OTJjZGYzZGYKZGF0YV9wb2ludB5jb20ubXl0aWtpLmV4YW1wbGVfZmx1dHRlcl90b3M5WW91IGNhbiBwdXQgdGhlIFRlcm1zIG9mIFNlcnZpY2UgdGhlIHVzZXIgYWdyZWVkIHRvIGhlcmUuC1sidXNlcl9pZCJd",
                found.getContents().getRaw());
    }

    @Test
    public void Test_GetTxn_Consent_Success() throws MalformedURLException, URISyntaxException {
        mockServer.expect(requestTo(new URI("http://localhost:8080/mockedBlock"))).andRespond(
                withStatus(HttpStatus.OK).body(MockedBlock.get()));

        BlockService blockService = new BlockService(blockRepository, testRestTemplate.getRestTemplate());
        TxnService txnService = new TxnService(txnRepository, blockService);

        String txnHash = "IR3N5-G9VU53R0zzcrI8mdB0_LoPPamM-VrqjKBRaRg";
        txnRepository.deleteByHash(B64.decode(txnHash));

        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();
        String blockHash = UUID.randomUUID().toString();
        URL src = new URL("http://localhost:8080/mockedBlock");

        AddressDO testAddress = new AddressDO();
        testAddress.setAddress(B64.decode(address));
        testAddress.setAid(appId);
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

        TxnAO<TxnAOContentsConsent> found = (TxnAO<TxnAOContentsConsent>) txnService.getTransaction(appId, address, blockHash, txnHash);

        assertEquals(appId, found.getApiId());
        assertEquals(address, found.getAddress());
        assertEquals(blockHash, found.getBlock());
        assertEquals(txnHash, found.getHash());
        assertEquals(src.toString(), found.getUrl());
        assertEquals(1, found.getVersion());
        assertEquals("AA", found.getAssetRef());
        assertEquals("2023-01-12T02:56:18Z", found.getTimestamp().toString());
        assertEquals("gjp8OXna2pe4G_gELtQr31W-Hucwr3a3F25w7Nfrg1I1ortZtYSK1Z-fJV2x-fhX3E_5ZfmKwT5x5WfFnFdJsEKuGIeJZBkL4JUU2GRSuz8xK9u8QXJIPRRIbJCHmFWPyTC5l7dU2oLR9eXWSGf7wpWYzXL_NLbgAEZ5mmimTHsJjwE8C5ZsiFk6kpEePxPTPWlBiBnHHk1sZUipTgzW02c3cndw9Vo3EEETPHVYhVhTSF5kkj0KIEYsVcT8t-Lv15raWIOVaJbdv5fEgZNIpx1HJ3Ss1NevGa3iaqeak-v3nnNsGKCmlh1iehXwUV02boobzD3yL91cvn6cdTO7BQ",
                found.getSignature());
        assertEquals(TxnContentSchema.CONSENT.getName(), found.getContentSchema());
        assertEquals("9G88VZEb7eyJSS5L71NLRfKN1Ab0Y3Sj6b-1azllfNM", found.getContents().getOwnershipId());
        assertEquals("\u0005[\"*\"]\u0005[\"*\"]", found.getContents().getDestination());
        assertNull(found.getContents().getAbout());
        assertNull(found.getContents().getReward());
        assertNull(found.getContents().getExpiry());
        assertEquals("IPRvPFWRG-3siUkuS-9TS0XyjdQG9GN0o-m_tWs5ZXzTDAVbIioiXQVbIioiXQEAAQABAA",
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
        testAddress.setAid(appId);
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

        TxnAO<?> found = txnService.getTransaction(appId, address, blockHash, txnHash);

        assertEquals(appId, found.getApiId());
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
        testAddress.setAid(appId);
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

        TxnAO<?> found = txnService.getTransaction(appId, address, blockHash, txnHash);

        assertEquals(appId, found.getApiId());
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

        TxnAO<?> found = txnService.getTransaction(appId, address, blockHash, txnHash);

        assertEquals(appId, found.getApiId());
        assertEquals(address, found.getAddress());
        assertEquals(blockHash, found.getBlock());
        assertEquals(txnHash, found.getHash());
    }
}

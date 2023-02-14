/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import com.mytiki.l0_index.features.latest.address.AddressDO;
import com.mytiki.l0_index.features.latest.address.AddressRepository;
import com.mytiki.l0_index.features.latest.block.BlockAO;
import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.block.BlockRepository;
import com.mytiki.l0_index.features.latest.block.BlockService;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {App.class}
)
@ActiveProfiles(profiles = {"ci", "dev", "local"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BlockTest {

    @Autowired
    private BlockRepository repository;

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
    public void Test_GetBlock_Success() throws MalformedURLException, URISyntaxException {
        mockServer.expect(requestTo(new URI("http://localhost:8080/mockedBlock"))).andRespond(
                withStatus(HttpStatus.OK).body(MockedBlock.get()));

        BlockService service = new BlockService(repository, testRestTemplate.getRestTemplate());

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
        repository.save(testBlock);

        BlockAO found = service.getBlock(appId, address, hash);
        assertEquals(appId, found.getAppId());
        assertEquals(address, found.getAddress());
        assertEquals(hash, found.getHash());
        assertEquals(src.toString(), found.getUrl());
        assertEquals(1, found.getVersion());
        assertEquals("AA", found.getPrevious());
        assertEquals("2023-02-14T04:11:03Z", found.getTimestamp().toString());
        assertEquals(
                "fyKzihI1aZ6QIc2BKMv5NIVrnzXNy1I2wx71yJS93heJQnvenECq5oCkMsUdsgpLe3ZZPnX8pwP_zPsp-R9P8zbx5aOhY2VVBuxPNzBKjn_rdZ_ShQau0qUPVmb6hWr8MN3aPE-n__7wgd9yjGap_l-C9rv2aBINF1GUotez9YkC4mmrA3lX4OEJJJB2LQrRyXYyKUlCqX5R5VkI_0_GkHpp2vfgTk8GyaNwJSZgIIEvxQT18jIAZ743mXd1481DlGbdg-WwZHtFLi7LBhWBTXTryN9hMuNK_hy_TYu7cqrXy1GGfWGIVWhYiVcz4ibuAhmxZ_GInVGmVwWzjlv54g",
                found.getSignature());
        assertEquals("4y3VPrsWN8m4CfQBNO8CK8OEAnwRD47ySkrjp3vOEiw", found.getTransactionRoot());
        assertEquals(10, found.getTransactions().size());
        assertTrue(found.getTransactions().contains("-TCPtpG11vITi0J3stisH834F4FCY65p1F5V4LtX75U"));
        assertTrue(found.getTransactions().contains("TfP2bf66oS0ysSGgqDA7JshI0QQBTmSds-2ngT2NisM"));
    }

    @Test
    public void Test_GetBlock_InvalidSrc() throws MalformedURLException, URISyntaxException {
        mockServer.expect(requestTo(new URI("http://localhost:8080/mockedBlock"))).andRespond(
                withStatus(HttpStatus.NOT_FOUND));

        BlockService service = new BlockService(repository, testRestTemplate.getRestTemplate());

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
        repository.save(testBlock);

        BlockAO found = service.getBlock(appId, address, hash);
        assertEquals(appId, found.getAppId());
        assertEquals(address, found.getAddress());
        assertEquals(hash, found.getHash());
        assertEquals(src.toString(), found.getUrl());
    }

    @Test
    public void Test_GetBlock_NoBlock() {
        BlockService service = new BlockService(repository, testRestTemplate.getRestTemplate());

        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();
        String hash = UUID.randomUUID().toString();

        BlockAO found = service.getBlock(appId, address, hash);
        assertEquals(appId, found.getAppId());
        assertEquals(address, found.getAddress());
        assertEquals(hash, found.getHash());
    }
}

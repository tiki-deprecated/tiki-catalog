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
        testAddress.setAid(appId);
        testAddress.setCreated(ZonedDateTime.now());
        addressRepository.save(testAddress);

        BlockDO testBlock = new BlockDO();
        testBlock.setAddress(testAddress);
        testBlock.setHash(B64.decode(hash));
        testBlock.setSrc(src);
        testBlock.setCreated(ZonedDateTime.now());
        repository.save(testBlock);

        BlockAO found = service.getBlock(appId, address, hash);
        assertEquals(appId, found.getApiId());
        assertEquals(address, found.getAddress());
        assertEquals(hash, found.getHash());
        assertEquals(src.toString(), found.getUrl());
        assertEquals(1, found.getVersion());
        assertEquals("AA", found.getPrevious());
        assertEquals("2023-01-12T02:56:19Z", found.getTimestamp().toString());
        assertEquals(
                "BCRu6A66rHsdMZ_wXDbIKSw26iwQ5tXpaAgKhDivPJcx264CRAoTHTBBojoyF5AVc7EjgzGC0kgKKgEIEfou3QsMrQ7d-YX4LRihuyolugjPBiPpUwpucoHc8LZ3qUZ5aHGN2t5ixkkD57BFd2FZWkboPb9oc0XjZMIxg0AuEJawoxDDafkfMJi15BJtNiYRHmThR2FLEtTC4ZNIYLmMemhPRoczbXiN70JAkTJ-2F38hEdysxRPYiSNJ3056Ppz8FhY4-tDORbyWkJw4WSVd3RSU4usAo0V-AxOxv32k-b0AZmZgOJCgcRxejLyFOuwWn3HGQXD5Yj_NYzifnamCw",
                found.getSignature());
        assertEquals("igw0LizBwrFiHPqHOm0KsNGptN97G0rKXmOZh7Mv1mc", found.getTransactionRoot());
        assertEquals(3, found.getTransactions().size());
        assertTrue(found.getTransactions().contains("9G88VZEb7eyJSS5L71NLRfKN1Ab0Y3Sj6b-1azllfNM"));
        assertTrue(found.getTransactions().contains("IR3N5-G9VU53R0zzcrI8mdB0_LoPPamM-VrqjKBRaRg"));
        assertTrue(found.getTransactions().contains("eCgm9X0b2g9ultCEL8lrb8lyE0q3m3VyVPm_NbJ__fk"));
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
        testAddress.setAid(appId);
        testAddress.setCreated(ZonedDateTime.now());
        addressRepository.save(testAddress);

        BlockDO testBlock = new BlockDO();
        testBlock.setAddress(testAddress);
        testBlock.setHash(B64.decode(hash));
        testBlock.setSrc(src);
        testBlock.setCreated(ZonedDateTime.now());
        repository.save(testBlock);

        BlockAO found = service.getBlock(appId, address, hash);
        assertEquals(appId, found.getApiId());
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
        assertEquals(appId, found.getApiId());
        assertEquals(address, found.getAddress());
        assertEquals(hash, found.getHash());
    }
}

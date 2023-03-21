/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import com.mytiki.l0_index.features.latest.address.AddressService;
import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.block.BlockRepository;
import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.features.latest.index.IndexAOReqTitle;
import com.mytiki.l0_index.features.latest.tag.TagService;
import com.mytiki.l0_index.features.latest.title.TitleAORsp;
import com.mytiki.l0_index.features.latest.title.TitleDO;
import com.mytiki.l0_index.features.latest.title.TitleRepository;
import com.mytiki.l0_index.features.latest.title.TitleService;
import com.mytiki.l0_index.main.App;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {App.class}
)
@ActiveProfiles(profiles = {"ci", "dev", "local"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TitleTest {
    @Autowired
    private TitleService service;

    @Autowired
    private BlockService blockService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TitleRepository repository;

    @Autowired
    private TagService tagService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private BlockRepository blockRepository;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(testRestTemplate.getRestTemplate());
    }

    @Test
    @Transactional
    public void Test_Insert_Success() {
        IndexAOReqTitle req = new IndexAOReqTitle(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), List.of(UUID.randomUUID().toString()));
        String appId = UUID.randomUUID().toString();
        BlockDO block = blockService.insert(UUID.randomUUID().toString(), "https://mytiki.com");
        TitleDO title = service.insert(req, appId, block);

        assertEquals(title.getTransaction(), req.getTransaction());
        assertEquals(title.getPtr(), req.getPtr());
        assertEquals(title.getAddress().getAddress(), req.getAddress());
        assertEquals(title.getAddress().getAppId(), appId);
        assertEquals(title.getBlock().getId(), block.getId());
        assertEquals(title.getTags().size(), req.getTags().size());
        assertEquals(title.getTags().get(0).getValue(), req.getTags().get(0));
        assertNotNull(title.getCreated());
        assertNotNull(title.getId());
    }

    @Test
    @Transactional
    public void Test_Insert_Existing_Success() {
        IndexAOReqTitle req = new IndexAOReqTitle(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), List.of(UUID.randomUUID().toString()));
        String appId = UUID.randomUUID().toString();
        BlockDO block = blockService.insert(UUID.randomUUID().toString(), "https://mytiki.com");

        service.insert(req, appId, block);
        TitleDO title = service.insert(req, appId, block);

        assertEquals(title.getTransaction(), req.getTransaction());
        assertEquals(title.getPtr(), req.getPtr());
        assertEquals(title.getAddress().getAddress(), req.getAddress());
        assertEquals(title.getAddress().getAppId(), appId);
        assertEquals(title.getBlock().getId(), block.getId());
        assertEquals(title.getTags().size(), req.getTags().size());
        assertEquals(title.getTags().get(0).getValue(), req.getTags().get(0));
        assertNotNull(title.getCreated());
        assertNotNull(title.getId());
    }

    @Test
    @Transactional
    public void Test_Fetch_Success() throws URISyntaxException {
        String id = "V5o2oRfogRHcegsJMN86E5Zf4Cz1tTvEkE3KK9qaM1U";
        mockServer.expect(requestTo(new URI("http://localhost:8080/mockedBlock"))).andRespond(
                withStatus(HttpStatus.OK).body(MockedBlock.get()));
        BlockService bservice = new BlockService(blockRepository, testRestTemplate.getRestTemplate());
        TitleService titleService = new TitleService(repository, tagService, addressService, bservice);
        IndexAOReqTitle req = new IndexAOReqTitle(id, UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), List.of(UUID.randomUUID().toString()));
        BlockDO block = bservice.insert(UUID.randomUUID().toString(), "http://localhost:8080/mockedBlock");
        String appId = UUID.randomUUID().toString();
        titleService.insert(req, appId, block);

        TitleAORsp title = titleService.fetch(id);
        assertEquals(title.getId(), id);
        assertEquals(title.getAddress(), req.getAddress());
        assertEquals(title.getTags().size(), 1);
        assertEquals(title.getTags().get(0), req.getTags().get(0));
        assertEquals(title.getOrigin(), "com.mytiki.sdk-dart.test");
        assertEquals(title.getPtr(), req.getPtr());
        assertEquals(title.getTimestamp().toString(), "2023-03-12T04:26:37Z");
        assertNull(title.getUser());
        assertNull(title.getDescription());
        assertEquals(title.getUserSignature().getSignature(),
                "bjd1U1ocKUWqh3OAJz3i6qtQi1IkGmE3oZJTc0rllp1BJS6b6StfGsavKgW4TLTqpk1GrFqhoPFuTqWZGNmnrTEoNWr1ZXTfMq8xZKzoQfuk5CP8bUfc5wfNg7Y3e8AVbU9iIxtLId1bLrKa1pn3luSu-IiXv_xZAAPXwpHvZVH20Ge7gQ25_ePmi6-5ApyrmtTJ1psGKxEUdCXLzbYDdSI9XsXQ2LX5AbbLe7kq6A9jZNg6HWfjkvUMXS8N6BxpzKBMcMTrY3fmDTdBbl5uFunRTdBSGA-NRr5QVuE022puokhZVRidFP3uAC6SlMO_7OZkFfQzm3NTqiR42rQ5fw");
        assertEquals(title.getUserSignature().getPubkey(),
                "https://bucket.storage.l0.mytiki.com/" + appId + "/"+ req.getAddress() + "/public.key");
        assertNull(title.getAppSignature());
    }

    @Test
    @Transactional
    public void Test_Fetch_None_Success() throws URISyntaxException {
        String id = UUID.randomUUID().toString();
        mockServer.expect(requestTo(new URI("http://localhost:8080/mockedBlock"))).andRespond(
                withStatus(HttpStatus.OK).body(MockedBlock.get()));
        BlockService bservice = new BlockService(blockRepository, testRestTemplate.getRestTemplate());
        TitleService titleService = new TitleService(repository, tagService, addressService, bservice);

        TitleAORsp title = titleService.fetch(id);
        assertNull(title.getId());
    }
}

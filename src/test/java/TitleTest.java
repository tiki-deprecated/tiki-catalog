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
        mockServer.expect(requestTo(new URI(MockedBlock.titleSrc))).andRespond(
                withStatus(HttpStatus.OK).body(MockedBlock.title()));
        BlockService bservice = new BlockService(blockRepository, testRestTemplate.getRestTemplate());
        TitleService titleService = new TitleService(repository, tagService, addressService, bservice);
        IndexAOReqTitle req = new IndexAOReqTitle(MockedBlock.titleTxn, MockedBlock.address,
                "dummy", List.of("dummy"));
        BlockDO block = bservice.insert(MockedBlock.titleHash, MockedBlock.titleSrc);
        titleService.insert(req, MockedBlock.appId, block);

        TitleAORsp title = titleService.fetch(MockedBlock.titleTxn, MockedBlock.appId);
        assertEquals(title.getId(), MockedBlock.titleTxn);
        assertEquals(title.getAddress(), req.getAddress());
        assertEquals(title.getTags().size(), 1);
        assertEquals(title.getTags().get(0), req.getTags().get(0));
        assertEquals(title.getOrigin(), "com.mytiki.sdk-dart.test");
        assertEquals(title.getPtr(), req.getPtr());
        assertEquals(title.getTimestamp().toString(), "2023-03-12T04:19:41Z");
        assertNull(title.getUser());
        assertNull(title.getDescription());
        assertEquals(title.getUserSignature().getSignature(),
                "OQVahD8QhYdkwgOD5LpmrbJm68W-fBumUCcW1ZoJyqeDmeIK5QIp4kn_AQFQds-m1h1hLC9luqYDp4pOsuzo4puj0gKHIf0NAnQGfdTXLepqxeUL1Xu6poHtafVdusAvNCGXh1MrvnUz1UW2oPYeQaKzl-EqkJ8jwyzmGqCZ_d_Y5zwtpACl7SFPzfr35tjVctgNAyyd1ye3iDGir0n0DTFGbOtGat3nY_ceJIW2jEAcpmHzN7fLX_TG1-5YH2VeQZu5NtUL4AqmCDeyPQNAp1XtA_wFDad8D1UxCQDICzwekVoNFJhaaM8coGNx-Sn0XIkm4tU2NsfHKCT3ZmiBWA");
        assertEquals(title.getUserSignature().getPubkey(),
                "https://bucket.storage.l0.mytiki.com/" +
                        MockedBlock.appId + "/"+ req.getAddress() + "/public.key");
        assertNull(title.getAppSignature());
    }

    @Test
    @Transactional
    public void Test_Fetch_None_Success() throws URISyntaxException {
        mockServer.expect(requestTo(new URI(MockedBlock.titleSrc))).andRespond(
                withStatus(HttpStatus.OK).body(MockedBlock.title()));
        BlockService bservice = new BlockService(blockRepository, testRestTemplate.getRestTemplate());
        TitleService titleService = new TitleService(repository, tagService, addressService, bservice);

        TitleAORsp title = titleService.fetch(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        assertNull(title.getId());
    }
}

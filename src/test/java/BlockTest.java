/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.block.BlockRepository;
import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.main.App;
import com.mytiki.spring_rest_api.ApiException;
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
public class BlockTest {
    @Autowired
    private BlockService service;

    @Autowired
    private BlockRepository repository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(testRestTemplate.getRestTemplate());
    }

    @Test
    public void Test_Insert_Success() {
        String hash = UUID.randomUUID().toString();
        String src = "https://mytiki.com/" + UUID.randomUUID();
        BlockDO inserted = service.insert(hash, src);

        assertEquals(hash, inserted.getHash());
        assertEquals(src, inserted.getSrc().toString());
        assertNotNull(inserted.getId());
        assertNotNull(inserted.getCreated());
    }

    @Test
    public void Test_Insert_Existing_Success() {
        String hash = UUID.randomUUID().toString();
        String src = "https://mytiki.com/" + UUID.randomUUID();
        service.insert(hash, src);
        BlockDO inserted = service.insert(hash, src);

        assertEquals(hash, inserted.getHash());
        assertEquals(src, inserted.getSrc().toString());
        assertNotNull(inserted.getId());
        assertNotNull(inserted.getCreated());
    }

    @Test
    public void Test_Insert_BadSrc_Failure() {
        String hash = UUID.randomUUID().toString();
        String src = UUID.randomUUID().toString();

        ApiException ex = assertThrows(ApiException.class, () -> service.insert(hash, src));
        assertEquals(ex.getHttpStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void Test_Fetch_Success() throws MalformedURLException, URISyntaxException {
        mockServer.expect(requestTo(new URI("http://localhost:8080/mockedBlock"))).andRespond(
                withStatus(HttpStatus.OK).body(MockedBlock.get()));

        BlockService service = new BlockService(repository, testRestTemplate.getRestTemplate());
        URL src = new URL("http://localhost:8080/mockedBlock");
        byte[] decoded = service.fetch(src, "V5o2oRfogRHcegsJMN86E5Zf4Cz1tTvEkE3KK9qaM1U");
        assertNotNull(decoded);
    }
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import com.mytiki.l0_index.features.latest.registry.RegistryService;
import com.mytiki.l0_index.main.App;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;

import java.net.URISyntaxException;
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
public class RegistryTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void init() {
        testRestTemplate.setUriTemplateHandler(new RootUriTemplateHandler("http://mock.fu/api/latest"));
        mockServer = MockRestServiceServer.createServer(testRestTemplate.getRestTemplate());
    }


    @Test
    public void Test_GetId_Success() throws URISyntaxException {
        String address = UUID.randomUUID().toString();
        String appId = UUID.randomUUID().toString();
        String path = "http://mock.fu/api/latest/address/" + address + "?app-id=" + appId;
        mockServer.expect(requestTo(path)).andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(MockedRegistry.json));

        RegistryService service = new RegistryService(testRestTemplate.getRestTemplate());
        String id = service.getId(address, appId);

        assertEquals("2edc9b17-11c0-406b-8335-0843b3b4183b", id);
    }

    @Test
    public void Test_NoId_Success() throws URISyntaxException {
        String address = UUID.randomUUID().toString();
        String appId = UUID.randomUUID().toString();
        String path = "http://mock.fu/api/latest/address/" + address + "?app-id=" + appId;
        mockServer.expect(requestTo(path)).andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON));

        RegistryService service = new RegistryService(testRestTemplate.getRestTemplate());

        String id = service.getId(address, appId);
        assertNull(id);
    }
}

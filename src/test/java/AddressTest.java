/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import com.mytiki.l0_index.features.latest.address.AddressDO;
import com.mytiki.l0_index.features.latest.address.AddressService;
import com.mytiki.l0_index.main.App;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {App.class}
)
@ActiveProfiles(profiles = {"ci", "dev", "local"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddressTest {
    @Autowired
    private AddressService service;

    @Test
    public void Test_Insert_Success() {
        String address = UUID.randomUUID().toString();
        String appId = UUID.randomUUID().toString();
        AddressDO inserted = service.insert(address, appId);

        assertEquals(inserted.getAddress(), address);
        assertEquals(inserted.getAppId(), appId);
        assertNotNull(inserted.getId());
        assertNotNull(inserted.getCreated());
    }

    @Test
    public void Test_Insert_Existing_Success() {
        String address = UUID.randomUUID().toString();
        String appId = UUID.randomUUID().toString();
        service.insert(address, appId);
        AddressDO inserted = service.insert(address, appId);

        assertEquals(inserted.getAddress(), address);
        assertEquals(inserted.getAppId(), appId);
        assertNotNull(inserted.getId());
        assertNotNull(inserted.getCreated());
    }
}

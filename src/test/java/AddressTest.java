/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import com.mytiki.l0_index.features.latest.address.AddressAO;
import com.mytiki.l0_index.features.latest.address.AddressDO;
import com.mytiki.l0_index.features.latest.address.AddressRepository;
import com.mytiki.l0_index.features.latest.address.AddressService;
import com.mytiki.l0_index.main.App;
import com.mytiki.l0_index.utilities.B64;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {App.class}
)
@ActiveProfiles(profiles = {"ci", "dev", "local"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddressTest {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private AddressService service;

    @Test
    public void Test_GetAddress_Success(){
        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();

        AddressDO testAddress = new AddressDO();
        testAddress.setAddress(B64.decode(address));
        testAddress.setAid(appId);
        testAddress.setCreated(ZonedDateTime.now());
        repository.save(testAddress);
        AddressAO found = service.getAddress(appId, address);

        assertEquals(appId, found.getAppId());
        assertEquals(address, found.getAddress());
    }

    @Test
    public void Test_GetAddress_NoAddress(){
        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();

        AddressAO found = service.getAddress(appId, address);
        assertNull(found);
    }
}

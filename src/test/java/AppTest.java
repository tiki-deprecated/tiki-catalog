/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import com.mytiki.l0_index.features.latest.address.AddressDO;
import com.mytiki.l0_index.features.latest.address.AddressRepository;
import com.mytiki.l0_index.features.latest.app.AppAO;
import com.mytiki.l0_index.features.latest.app.AppService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {App.class}
)
@ActiveProfiles(profiles = {"ci", "dev", "local"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppTest {
    @Autowired
    private AddressRepository repository;

    @Autowired
    private AppService service;

    @Test
    public void Test_GetApp_Single_Success(){
        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();

        AddressDO testAddress = new AddressDO();
        testAddress.setAddress(B64.decode(address));
        testAddress.setAid(appId);
        testAddress.setCreated(ZonedDateTime.now());
        repository.save(testAddress);

        AppAO found = service.getApp(appId);
        assertEquals(appId, found.getAppId());
        assertEquals(1, found.getAddress().size());
        assertTrue(found.getAddress().contains(address));
    }

    @Test
    public void Test_GetApp_Multiple_Success(){
        int rounds = 3;
        String appId = UUID.randomUUID().toString();

        List<String> addressList = new ArrayList<>(){{
            add(UUID.randomUUID().toString());
            add(UUID.randomUUID().toString());
            add(UUID.randomUUID().toString());
        }};

        for(int i=0; i<rounds; i++) {
            AddressDO testAddress = new AddressDO();
            testAddress.setAddress(B64.decode(addressList.get(i)));
            testAddress.setAid(appId);
            testAddress.setCreated(ZonedDateTime.now());
            repository.save(testAddress);
        }

        AppAO found = service.getApp(appId);
        assertEquals(appId, found.getAppId());
        assertEquals(rounds, found.getAddress().size());
        for(int i=0; i<rounds; i++){
            assertTrue(addressList.contains(found.getAddress().get(i)));
        }
    }

    @Test
    public void Test_GetApp_None_Success(){
        String appId = UUID.randomUUID().toString();

        AppAO found = service.getApp(appId);
        assertEquals(appId, appId);
        assertEquals(0, found.getAddress().size());
    }
}

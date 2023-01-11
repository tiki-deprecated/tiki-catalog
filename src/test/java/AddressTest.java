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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void Test_GetAddresses_Single(){
        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();

        AddressDO testAddress = new AddressDO();
        testAddress.setAddress(B64.decode(address));
        testAddress.setAid(appId);
        testAddress.setCreated(ZonedDateTime.now());
        repository.save(testAddress);

        List<AddressAO> found = service.getAddresses(address);
        assertEquals(1, found.size());
        assertEquals(appId, found.get(0).getAppId());
        assertEquals(address, found.get(0).getAddress());
    }

    @Test
    public void Test_GetAddresses_Multiple(){
        int rounds = 3;
        String address = UUID.randomUUID().toString();

        List<String> aidList = new ArrayList<>(){{
            add(UUID.randomUUID().toString());
            add(UUID.randomUUID().toString());
            add(UUID.randomUUID().toString());
        }};

        for(int i=0; i<rounds; i++) {
            AddressDO testAddress = new AddressDO();
            testAddress.setAddress(B64.decode(address));
            testAddress.setAid(aidList.get(i));
            testAddress.setCreated(ZonedDateTime.now());
            repository.save(testAddress);
        }

        List<AddressAO> found = service.getAddresses(address);
        assertEquals(rounds, found.size());
        for(int i=0; i<rounds; i++){
            assertEquals(address, found.get(i).getAddress());
            assertTrue(aidList.contains(found.get(i).getAppId()));
        }
    }

    @Test
    public void Test_GetAddresses_None(){
        String address = UUID.randomUUID().toString();

        List<AddressAO> found = service.getAddresses(address);
        assertEquals(0, found.size());
    }
}

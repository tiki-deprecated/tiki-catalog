/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import com.mytiki.l0_index.features.latest.address.AddressAO;
import com.mytiki.l0_index.features.latest.address.AddressDO;
import com.mytiki.l0_index.features.latest.address.AddressRepository;
import com.mytiki.l0_index.features.latest.address.AddressService;
import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.block.BlockRepository;
import com.mytiki.l0_index.main.App;
import com.mytiki.l0_index.utilities.B64;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.net.MalformedURLException;
import java.net.URL;
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
    private BlockRepository blockRepository;

    @Autowired
    private AddressService service;

    @Test
    public void Test_GetAddress_NoBlocks_Success(){
        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();

        AddressDO testAddress = new AddressDO();
        testAddress.setAddress(B64.decode(address));
        testAddress.setAppId(appId);
        testAddress.setCreated(ZonedDateTime.now());
        repository.save(testAddress);
        AddressAO found = service.getAddress(appId, address, 0, 100);

        assertEquals(appId, found.getAppId());
        assertEquals(address, found.getAddress());
        assertEquals(0, found.getBlocks().getPage());
        assertEquals(0, found.getBlocks().getTotalPages());
        assertEquals(0, found.getBlocks().getTotalHashes());
        assertEquals(0, found.getBlocks().getHashes().size());
    }

    @Test
    public void Test_GetAddress_NoAddress_Success(){
        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();

        AddressAO found = service.getAddress(appId, address, 0, 100);
        assertEquals(appId, found.getAppId());
        assertNull(found.getAddress());
        assertNull(found.getBlocks());
    }

    @Test
    public void Test_GetAddress_Success() throws MalformedURLException {
        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();

        AddressDO testAddress = new AddressDO();
        testAddress.setAddress(B64.decode(address));
        testAddress.setAppId(appId);
        testAddress.setCreated(ZonedDateTime.now());
        repository.save(testAddress);

        int blocks = 10;
        List<String> hashes = new ArrayList<>(blocks);
        for(int i =0; i<blocks; i++){
            BlockDO block = new BlockDO();
            block.setAddress(testAddress);
            block.setCreated(ZonedDateTime.now());
            block.setSrc(new URL("https://mytiki.com"));
            String hash = UUID.randomUUID().toString();
            block.setHash(B64.decode(hash));
            hashes.add(hash);
            blockRepository.save(block);
        }

        AddressAO found = service.getAddress(appId, address, 0, 100);

        assertEquals(appId, found.getAppId());
        assertEquals(address, found.getAddress());
        assertEquals(blocks, found.getBlocks().getTotalHashes());
        assertEquals(1, found.getBlocks().getTotalPages());
        assertEquals(0, found.getBlocks().getPage());

        for(int i=0; i<blocks; i++){
           assertTrue(hashes.contains(found.getBlocks().getHashes().get(i)));
        }
    }

    @Test
    public void Test_GetAddress_Page_Success() throws MalformedURLException {
        String appId = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();

        AddressDO testAddress = new AddressDO();
        testAddress.setAddress(B64.decode(address));
        testAddress.setAppId(appId);
        testAddress.setCreated(ZonedDateTime.now());
        repository.save(testAddress);

        int blocks = 10;
        List<String> hashes = new ArrayList<>(blocks);
        for(int i =0; i<blocks; i++){
            BlockDO block = new BlockDO();
            block.setAddress(testAddress);
            block.setCreated(ZonedDateTime.now());
            block.setSrc(new URL("https://mytiki.com"));
            String hash = UUID.randomUUID().toString();
            block.setHash(B64.decode(hash));
            hashes.add(hash);
            blockRepository.save(block);
        }

        AddressAO found = service.getAddress(appId, address, 2, 3);

        assertEquals(appId, found.getAppId());
        assertEquals(address, found.getAddress());
        assertEquals(blocks, found.getBlocks().getTotalHashes());
        assertEquals(4, found.getBlocks().getTotalPages());
        assertEquals(2, found.getBlocks().getPage());

        for(int i=0; i<found.getBlocks().getHashes().size(); i++){
            assertTrue(hashes.contains(found.getBlocks().getHashes().get(i)));
        }
    }
}

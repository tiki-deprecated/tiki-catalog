/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.features.latest.index.IndexAOReqLicense;
import com.mytiki.l0_index.features.latest.index.IndexAOReqTitle;
import com.mytiki.l0_index.features.latest.license.*;
import com.mytiki.l0_index.features.latest.title.TitleDO;
import com.mytiki.l0_index.features.latest.title.TitleService;
import com.mytiki.l0_index.main.App;
import com.mytiki.l0_index.utilities.AOUse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
public class LicenseTest {
    @Autowired
    private LicenseService service;

    @Autowired
    private TitleService titleService;

    @Autowired
    private BlockService blockService;

    @Test
    @Transactional
    public void Test_Insert_Success() {
        String appId = UUID.randomUUID().toString();
        BlockDO block = blockService.insert(UUID.randomUUID().toString(), "https://mytiki.com");
        IndexAOReqTitle titleReq = new IndexAOReqTitle(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), List.of(UUID.randomUUID().toString()));
        TitleDO title = titleService.insert(titleReq, appId, block);

        IndexAOReqLicense req = new IndexAOReqLicense(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                title.getTransaction(), List.of(new AOUse(UUID.randomUUID().toString(), null)));
        LicenseDO license = service.insert(req, appId, block);

        assertEquals(license.getTransaction(), req.getTransaction());
        assertEquals(license.getAddress().getAddress(), req.getAddress());
        assertEquals(license.getAddress().getAppId(), appId);
        assertEquals(license.getBlock().getId(), block.getId());
        assertEquals(license.getUses().size(), 1);
        assertEquals(license.getUses().get(0).getUsecase(), req.getUses().get(0).getUsecase());
        assertEquals(license.getUses().get(0).getDestination(), req.getUses().get(0).getDestination());
        assertEquals(license.getTitle().getId(), title.getId());
        assertNotNull(license.getCreated());
        assertNotNull(license.getId());
    }

    @Test
    @Transactional
    public void Test_Insert_Existing_Success() {
        String appId = UUID.randomUUID().toString();
        BlockDO block = blockService.insert(UUID.randomUUID().toString(), "https://mytiki.com");
        IndexAOReqTitle titleReq = new IndexAOReqTitle(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), List.of(UUID.randomUUID().toString()));
        TitleDO title = titleService.insert(titleReq, appId, block);

        IndexAOReqLicense req = new IndexAOReqLicense(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                title.getTransaction(), List.of(new AOUse(UUID.randomUUID().toString(), null)));

        service.insert(req, appId, block);
        LicenseDO license = service.insert(req, appId, block);

        assertEquals(license.getTransaction(), req.getTransaction());
        assertEquals(license.getAddress().getAddress(), req.getAddress());
        assertEquals(license.getAddress().getAppId(), appId);
        assertEquals(license.getBlock().getId(), block.getId());
        assertEquals(license.getUses().size(), 1);
        assertEquals(license.getUses().get(0).getUsecase(), req.getUses().get(0).getUsecase());
        assertEquals(license.getUses().get(0).getDestination(), req.getUses().get(0).getDestination());
        assertEquals(license.getTitle().getId(), title.getId());
        assertNotNull(license.getCreated());
        assertNotNull(license.getId());
    }

    @Test
    @Transactional
    public void Test_Insert_NoTitle_Success() {
        String appId = UUID.randomUUID().toString();
        BlockDO block = blockService.insert(UUID.randomUUID().toString(), "https://mytiki.com");
        IndexAOReqLicense req = new IndexAOReqLicense(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
               UUID.randomUUID().toString(), List.of(new AOUse(UUID.randomUUID().toString(), null)));

        service.insert(req, appId, block);
        LicenseDO license = service.insert(req, appId, block);

        assertEquals(license.getTransaction(), req.getTransaction());
        assertEquals(license.getAddress().getAddress(), req.getAddress());
        assertEquals(license.getAddress().getAppId(), appId);
        assertEquals(license.getBlock().getId(), block.getId());
        assertEquals(license.getUses().size(), 1);
        assertEquals(license.getUses().get(0).getUsecase(), req.getUses().get(0).getUsecase());
        assertEquals(license.getUses().get(0).getDestination(), req.getUses().get(0).getDestination());
        assertNull(license.getTitle());
        assertNotNull(license.getCreated());
        assertNotNull(license.getId());
    }

    @Test
    public void Test_List_Page_Success(){
        String appId = UUID.randomUUID().toString();
        BlockDO block = blockService.insert(UUID.randomUUID().toString(), "https://mytiki.com");
        IndexAOReqTitle titleReq = new IndexAOReqTitle(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), List.of(UUID.randomUUID().toString()));
        TitleDO title = titleService.insert(titleReq, appId, block);

        int numLicenses = 10;
        List<String> ids = new ArrayList<>(numLicenses);
        for(int i=0; i<numLicenses; i++){
            String transaction = UUID.randomUUID().toString();
            IndexAOReqLicense req = new IndexAOReqLicense(
                    transaction, UUID.randomUUID().toString(), title.getTransaction(),
                    List.of(new AOUse(UUID.randomUUID().toString(), null)));
            service.insert(req, appId, block);
            ids.add(transaction);
        }

        LicenseAOReq req = new LicenseAOReq();
        req.setIncludeAll(true);

        LicenseAORspList list = service.list(req, appId, null, 2);
        List<LicenseAORspResult> res = new ArrayList<>(list.getResults());
        while(list.getNextPageToken() != null){
            list = service.list(req, appId, list.getNextPageToken(), 2);
            res.addAll(list.getResults());
        }

        assertEquals(res.size(), numLicenses);
        for(int i=0; i<numLicenses; i++){
            assertTrue(ids.contains(res.get(i).getId()));
        }
    }

    @Test
    public void Test_List_Latest_Success(){
        String appId = UUID.randomUUID().toString();
        BlockDO block = blockService.insert(UUID.randomUUID().toString(), "https://mytiki.com");
        IndexAOReqTitle titleReq = new IndexAOReqTitle(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), List.of(UUID.randomUUID().toString()));
        TitleDO title = titleService.insert(titleReq, appId, block);
        IndexAOReqLicense licenseReq1 = new IndexAOReqLicense(
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), title.getTransaction(),
                List.of(new AOUse(UUID.randomUUID().toString(), null)));
        IndexAOReqLicense licenseReq2 = new IndexAOReqLicense(
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), title.getTransaction(),
                List.of(new AOUse(UUID.randomUUID().toString(), null)));
        service.insert(licenseReq1, appId, block);
        service.insert(licenseReq2, appId, block);

        LicenseAOReq req = new LicenseAOReq();
        req.setIncludeAll(false);
        LicenseAORspList list = service.list(req, appId, null, 100);
        assertEquals(list.getResults().size(), 1);
        assertEquals(list.getResults().get(0).getId(), licenseReq2.getTransaction());
    }

    @Test
    public void Test_List_Tag_Success(){
        String appId = UUID.randomUUID().toString();
        BlockDO block = blockService.insert(UUID.randomUUID().toString(), "https://mytiki.com");
        IndexAOReqTitle titleReq = new IndexAOReqTitle(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), List.of(UUID.randomUUID().toString()));
        TitleDO title = titleService.insert(titleReq, appId, block);
        IndexAOReqLicense licenseReq = new IndexAOReqLicense(
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), title.getTransaction(),
                List.of(new AOUse(UUID.randomUUID().toString(), null)));
        service.insert(licenseReq, appId, block);

        LicenseAOReq req = new LicenseAOReq();
        req.setTags(titleReq.getTags());
        LicenseAORspList list = service.list(req, appId, null, 100);
        assertEquals(list.getResults().size(), 1);
        assertEquals(list.getResults().get(0).getId(), licenseReq.getTransaction());
    }

    @Test
    public void Test_List_Ptr_Success(){
        String appId = UUID.randomUUID().toString();
        BlockDO block = blockService.insert(UUID.randomUUID().toString(), "https://mytiki.com");
        IndexAOReqTitle titleReq = new IndexAOReqTitle(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), List.of(UUID.randomUUID().toString()));
        TitleDO title = titleService.insert(titleReq, appId, block);
        IndexAOReqLicense licenseReq = new IndexAOReqLicense(
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), title.getTransaction(),
                List.of(new AOUse(UUID.randomUUID().toString(), null)));
        service.insert(licenseReq, appId, block);

        LicenseAOReq req = new LicenseAOReq();
        req.setPtrs(List.of(titleReq.getPtr()));
        LicenseAORspList list = service.list(req, appId, null, 100);
        assertEquals(list.getResults().size(), 1);
        assertEquals(list.getResults().get(0).getId(), licenseReq.getTransaction());
    }

    @Test
    public void Test_List_Usecase_Success(){
        String appId = UUID.randomUUID().toString();
        BlockDO block = blockService.insert(UUID.randomUUID().toString(), "https://mytiki.com");
        IndexAOReqTitle titleReq = new IndexAOReqTitle(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), List.of(UUID.randomUUID().toString()));
        TitleDO title = titleService.insert(titleReq, appId, block);
        IndexAOReqLicense licenseReq = new IndexAOReqLicense(
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), title.getTransaction(),
                List.of(new AOUse(UUID.randomUUID().toString(), null)));
        service.insert(licenseReq, appId, block);

        LicenseAOReq req = new LicenseAOReq();
        req.setUsecases(List.of(licenseReq.getUses().get(0).getUsecase()));
        LicenseAORspList list = service.list(req, appId, null, 100);
        assertEquals(list.getResults().size(), 1);
        assertEquals(list.getResults().get(0).getId(), licenseReq.getTransaction());
    }

    @Test
    public void Test_List_Destination_Success(){
        String appId = UUID.randomUUID().toString();
        BlockDO block = blockService.insert(UUID.randomUUID().toString(), "https://mytiki.com");
        IndexAOReqTitle titleReq = new IndexAOReqTitle(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), List.of(UUID.randomUUID().toString()));
        TitleDO title = titleService.insert(titleReq, appId, block);
        IndexAOReqLicense licenseReq = new IndexAOReqLicense(
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), title.getTransaction(),
                List.of(new AOUse(UUID.randomUUID().toString(), UUID.randomUUID().toString())));
        service.insert(licenseReq, appId, block);

        LicenseAOReq req = new LicenseAOReq();
        req.setDestinations(List.of(licenseReq.getUses().get(0).getDestination()));
        LicenseAORspList list = service.list(req, appId, null, 100);
        assertEquals(list.getResults().size(), 1);
        assertEquals(list.getResults().get(0).getId(), licenseReq.getTransaction());
    }
}

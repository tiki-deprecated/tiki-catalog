/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.features.latest.index.IndexAOLicense;
import com.mytiki.l0_index.features.latest.index.IndexAOLicenseUse;
import com.mytiki.l0_index.features.latest.index.IndexAOTitle;
import com.mytiki.l0_index.features.latest.license.LicenseDO;
import com.mytiki.l0_index.features.latest.license.LicenseService;
import com.mytiki.l0_index.features.latest.title.TitleDO;
import com.mytiki.l0_index.features.latest.title.TitleService;
import com.mytiki.l0_index.main.App;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
        IndexAOTitle titleReq = new IndexAOTitle(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), List.of(UUID.randomUUID().toString()));
        TitleDO title = titleService.insert(titleReq, appId, block);

        IndexAOLicense req = new IndexAOLicense(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                title.getTransaction(), List.of(new IndexAOLicenseUse(UUID.randomUUID().toString(), null)));
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
        IndexAOTitle titleReq = new IndexAOTitle(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), List.of(UUID.randomUUID().toString()));
        TitleDO title = titleService.insert(titleReq, appId, block);

        IndexAOLicense req = new IndexAOLicense(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                title.getTransaction(), List.of(new IndexAOLicenseUse(UUID.randomUUID().toString(), null)));

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
        IndexAOLicense req = new IndexAOLicense(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
               UUID.randomUUID().toString(), List.of(new IndexAOLicenseUse(UUID.randomUUID().toString(), null)));

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
}

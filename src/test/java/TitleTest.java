/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import com.mytiki.l0_index.features.latest.block.BlockDO;
import com.mytiki.l0_index.features.latest.block.BlockService;
import com.mytiki.l0_index.features.latest.index.IndexAOTitle;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    @Transactional
    public void Test_Insert_Success() {
        IndexAOTitle req = new IndexAOTitle(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
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
        IndexAOTitle req = new IndexAOTitle(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
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
}

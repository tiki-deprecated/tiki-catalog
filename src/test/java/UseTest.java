/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import com.mytiki.l0_index.features.latest.index.IndexAOLicenseUse;
import com.mytiki.l0_index.features.latest.use.UseDO;
import com.mytiki.l0_index.features.latest.use.UseService;
import com.mytiki.l0_index.main.App;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {App.class}
)
@ActiveProfiles(profiles = {"ci", "dev", "local"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UseTest {
    @Autowired
    private UseService service;

    @Test
    public void Test_Insert_One_Success() {
        IndexAOLicenseUse use = new IndexAOLicenseUse(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        List<UseDO> inserted = service.insert(List.of(use));

        assertEquals(inserted.size(), 1);
        assertEquals(inserted.get(0).getUsecase(), use.getUsecase());
        assertEquals(inserted.get(0).getDestination(), use.getDestination());
        assertNotNull(inserted.get(0).getId());
        assertNotNull(inserted.get(0).getCreated());
    }

    @Test
    public void Test_Insert_All_Success() {
        int numUses = 5;
        List<IndexAOLicenseUse> toInsert = new ArrayList<>(numUses);
        Map<String, IndexAOLicenseUse> composite = new HashMap<>(numUses);
        for(int i=0; i<numUses; i++){
            IndexAOLicenseUse use = new IndexAOLicenseUse(UUID.randomUUID().toString(), UUID.randomUUID().toString());
            toInsert.add(use);
            composite.put(use.getUsecase() + ":" + use.getDestination(), use);
        }
        List<UseDO> inserted = service.insert(toInsert);

        assertEquals(inserted.size(), numUses);
        for(int i=0; i<numUses; i++){
            UseDO use = inserted.get(i);
            IndexAOLicenseUse req = composite.get(use.getUsecase() + ":" + use.getDestination());
            assertEquals(use.getDestination(), req.getDestination());
            assertEquals(use.getUsecase(), req.getUsecase());
        }
    }

    @Test
    public void Test_Insert_All_Existing_Success() {
        int numUses = 5;
        List<IndexAOLicenseUse> toInsert = new ArrayList<>(numUses);
        Map<String, IndexAOLicenseUse> composite = new HashMap<>(numUses);
        for(int i=0; i<numUses; i++){
            IndexAOLicenseUse use = new IndexAOLicenseUse(UUID.randomUUID().toString(), UUID.randomUUID().toString());
            toInsert.add(use);
            composite.put(use.getUsecase() + ":" + use.getDestination(), use);
        }

        service.insert(toInsert);
        List<UseDO> inserted = service.insert(toInsert);

        assertEquals(inserted.size(), numUses);
        for(int i=0; i<numUses; i++){
            UseDO use = inserted.get(i);
            IndexAOLicenseUse req = composite.get(use.getUsecase() + ":" + use.getDestination());
            assertEquals(use.getDestination(), req.getDestination());
            assertEquals(use.getUsecase(), req.getUsecase());
        }
    }

    @Test
    public void Test_Insert_Some_Existing_Success() {
        int numUses = 5;
        List<IndexAOLicenseUse> toInsert = new ArrayList<>(numUses * 2);
        Map<String, IndexAOLicenseUse> composite = new HashMap<>(numUses*2);
        for(int i=0; i<numUses; i++){
            IndexAOLicenseUse use = new IndexAOLicenseUse(UUID.randomUUID().toString(), UUID.randomUUID().toString());
            toInsert.add(use);
            composite.put(use.getUsecase() + ":" + use.getDestination(), use);
        }
        service.insert(toInsert);

        for(int i=numUses; i<(numUses*2); i++){
            IndexAOLicenseUse use = new IndexAOLicenseUse(UUID.randomUUID().toString(), UUID.randomUUID().toString());
            toInsert.add(use);
            composite.put(use.getUsecase() + ":" + use.getDestination(), use);
        }

        List<UseDO> inserted = service.insert(toInsert);

        assertEquals(inserted.size(), numUses * 2);
        for(int i=0; i<numUses; i++){
            UseDO use = inserted.get(i);
            IndexAOLicenseUse req = composite.get(use.getUsecase() + ":" + use.getDestination());
            assertEquals(use.getDestination(), req.getDestination());
            assertEquals(use.getUsecase(), req.getUsecase());
        }
    }

    @Test
    public void Test_Insert_Dupes_Success() {
        IndexAOLicenseUse use = new IndexAOLicenseUse(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        List<IndexAOLicenseUse> toInsert = List.of(use, use, use, use, use);

        List<UseDO> inserted = service.insert(toInsert);
        assertEquals(inserted.size(), 1);
        assertEquals(inserted.get(0).getDestination(), use.getDestination());
        assertEquals(inserted.get(0).getUsecase(), use.getUsecase());
    }

    @Test
    public void Test_Insert_NoDestination_Success() {
        IndexAOLicenseUse use = new IndexAOLicenseUse(UUID.randomUUID().toString(), null);
        List<IndexAOLicenseUse> toInsert = List.of(use);
        List<UseDO> inserted = service.insert(toInsert);

        assertEquals(inserted.size(), 1);
        assertEquals(inserted.get(0).getDestination(), use.getDestination());
        assertEquals(inserted.get(0).getUsecase(), use.getUsecase());
    }

    @Test
    public void Test_Insert_Exists_NoDestination_Success() {
        IndexAOLicenseUse use = new IndexAOLicenseUse(UUID.randomUUID().toString(), null);
        List<IndexAOLicenseUse> toInsert = List.of(use);
        service.insert(toInsert);
        List<UseDO> inserted = service.insert(toInsert);

        assertEquals(inserted.size(), 1);
        assertEquals(inserted.get(0).getDestination(), use.getDestination());
        assertEquals(inserted.get(0).getUsecase(), use.getUsecase());
    }

    @Test
    public void Test_Insert_Multiple_NoDestination_Success() {
        IndexAOLicenseUse use1 = new IndexAOLicenseUse(UUID.randomUUID().toString(), null);
        IndexAOLicenseUse use2 = new IndexAOLicenseUse(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        List<IndexAOLicenseUse> toInsert = List.of(use1, use2);
        service.insert(toInsert);
        List<UseDO> inserted = service.insert(toInsert);

        assertEquals(inserted.size(), 2);
    }
}

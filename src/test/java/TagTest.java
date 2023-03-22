/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import com.mytiki.l0_index.features.latest.tag.TagDO;
import com.mytiki.l0_index.features.latest.tag.TagService;
import com.mytiki.l0_index.main.App;
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
public class TagTest {
    @Autowired
    private TagService service;

    @Test
    public void Test_Insert_One_Success() {
        List<String> toInsert = List.of(UUID.randomUUID().toString());
        List<TagDO> inserted = service.insert(toInsert);

        assertEquals(inserted.size(), 1);
        assertEquals(inserted.get(0).getValue(), toInsert.get(0));
        assertNotNull(inserted.get(0).getId());
        assertNotNull(inserted.get(0).getCreated());
    }

    @Test
    public void Test_Insert_All_Success() {
        int numTags = 5;
        List<String> toInsert = new ArrayList<>(numTags);
        for(int i=0; i<numTags; i++){
            toInsert.add(UUID.randomUUID().toString());
        }
        List<TagDO> inserted = service.insert(toInsert);

        assertEquals(inserted.size(), numTags);
        for(int i=0; i<numTags; i++){
            assertTrue(toInsert.contains(inserted.get(i).getValue()));
        }
    }

    @Test
    public void Test_Insert_All_Existing_Success() {
        int numTags = 5;
        List<String> toInsert = new ArrayList<>(numTags);
        for(int i=0; i<numTags; i++){
            toInsert.add(UUID.randomUUID().toString());
        }

        service.insert(toInsert);
        List<TagDO> inserted = service.insert(toInsert);

        assertEquals(inserted.size(), numTags);
        for(int i=0; i<numTags; i++){
            assertTrue(toInsert.contains(inserted.get(i).getValue()));
        }
    }

    @Test
    public void Test_Insert_Some_Existing_Success() {
        int numTags = 5;
        List<String> toInsert = new ArrayList<>(numTags * 2);
        for(int i=0; i<numTags; i++){
            toInsert.add(UUID.randomUUID().toString());
        }

        service.insert(toInsert);
        for(int i=numTags; i<(numTags*2); i++){
            toInsert.add(UUID.randomUUID().toString());
        }
        List<TagDO> inserted = service.insert(toInsert);

        assertEquals(inserted.size(), numTags * 2);
        for(int i=0; i<numTags; i++){
            assertTrue(toInsert.contains(inserted.get(i).getValue()));
        }
    }

    @Test
    public void Test_Insert_Dupes_Success() {
        String tag = UUID.randomUUID().toString();
        List<String> toInsert = List.of(tag, tag, tag);

        List<TagDO> inserted = service.insert(toInsert);
        assertEquals(inserted.size(), 1);
        assertEquals(inserted.get(0).getValue(), tag);
    }
}

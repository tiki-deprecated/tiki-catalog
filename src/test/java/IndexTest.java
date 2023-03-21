/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import com.mytiki.l0_index.features.latest.index.IndexAOReq;
import com.mytiki.l0_index.features.latest.index.IndexAOReqLicense;
import com.mytiki.l0_index.features.latest.index.IndexAOReqTitle;
import com.mytiki.l0_index.features.latest.index.IndexService;
import com.mytiki.l0_index.features.latest.license.LicenseDO;
import com.mytiki.l0_index.features.latest.license.LicenseRepository;
import com.mytiki.l0_index.features.latest.title.TitleDO;
import com.mytiki.l0_index.features.latest.title.TitleRepository;
import com.mytiki.l0_index.main.App;
import com.mytiki.l0_index.utilities.AOUse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {App.class}
)
@ActiveProfiles(profiles = {"ci", "dev", "local"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IndexTest {

    @Autowired
    private IndexService service;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private TitleRepository titleRepository;

    @Test
    public void Test_Title_Success() {
        IndexAOReq req = new IndexAOReq(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "https://mytiki.com",
                List.of(new IndexAOReqTitle(
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        List.of(UUID.randomUUID().toString()))), null);

        service.index(req);
        Optional<TitleDO> title = titleRepository.getByTransaction(req.getTitles().get(0).getTransaction());
        assertTrue(title.isPresent());
    }

    @Test
    public void Test_License_Success() {
        IndexAOReq req = new IndexAOReq(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                "https://mytiki.com", null,
                List.of(new IndexAOReqLicense(
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        List.of(new AOUse(UUID.randomUUID().toString(), null)))));

        service.index(req);
        Optional<LicenseDO> license = licenseRepository.getByTransaction(req.getLicenses().get(0).getTransaction());
        assertTrue(license.isPresent());
    }

    @Test
    public void Test_Multiple_Success() {
        IndexAOReq req = new IndexAOReq(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                "https://mytiki.com",
                List.of(new IndexAOReqTitle(
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        List.of(UUID.randomUUID().toString()))),
                List.of(new IndexAOReqLicense(
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        List.of(new AOUse(UUID.randomUUID().toString(), null)))));

        service.index(req);
        Optional<TitleDO> title = titleRepository.getByTransaction(req.getTitles().get(0).getTransaction());
        assertTrue(title.isPresent());
        Optional<LicenseDO> license = licenseRepository.getByTransaction(req.getLicenses().get(0).getTransaction());
        assertTrue(license.isPresent());
    }
}

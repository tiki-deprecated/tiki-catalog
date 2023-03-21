/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.license;

import java.util.List;

public interface LicenseRepositorySearch {
    List<LicenseDO> search(LicenseAOReq req, String appId, Long licenseId, Integer maxResults);
}

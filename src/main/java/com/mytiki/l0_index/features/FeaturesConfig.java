/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features;

import com.mytiki.l0_index.features.latest.address.AddressConfig;
import com.mytiki.l0_index.features.latest.block.BlockConfig;
import com.mytiki.l0_index.features.latest.index.IndexConfig;
import com.mytiki.l0_index.features.latest.license.LicenseConfig;
import com.mytiki.l0_index.features.latest.tag.TagConfig;
import com.mytiki.l0_index.features.latest.title.TitleConfig;
import com.mytiki.l0_index.features.latest.use.UseConfig;
import org.springframework.context.annotation.Import;

@Import({
        AddressConfig.class,
        BlockConfig.class,
        IndexConfig.class,
        LicenseConfig.class,
        TagConfig.class,
        TitleConfig.class,
        UseConfig.class
})
public class FeaturesConfig {}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features;

import com.mytiki.l0_index.features.latest.address.AddressConfig;
import com.mytiki.l0_index.features.latest.app.AppConfig;
import org.springframework.context.annotation.Import;

@Import({
        AppConfig.class,
        AddressConfig.class,
})
public class FeaturesConfig {}

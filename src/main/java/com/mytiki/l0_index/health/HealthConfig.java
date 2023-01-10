/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.health;

import org.springframework.context.annotation.Bean;

public class HealthConfig {
    @Bean
    public HealthController healthController() {
        return new HealthController();
    }
}

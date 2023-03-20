/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.use;

import com.mytiki.l0_index.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(UseConfig.PACKAGE_PATH)
@EntityScan(UseConfig.PACKAGE_PATH)
public class UseConfig {
    public static final String PACKAGE_PATH = Constants.PACKAGE_FEATURES_LATEST_DOT_PATH + ".use";

    @Bean
    public final UseService useService(@Autowired UseRepository repository){
        return new UseService(repository);
    }
}

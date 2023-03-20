/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.tag;

import com.mytiki.l0_index.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(TagConfig.PACKAGE_PATH)
@EntityScan(TagConfig.PACKAGE_PATH)
public class TagConfig {
    public static final String PACKAGE_PATH = Constants.PACKAGE_FEATURES_LATEST_DOT_PATH + ".tag";

    @Bean
    public TagService tagService(@Autowired TagRepository repository){
        return new TagService(repository);
    }
}

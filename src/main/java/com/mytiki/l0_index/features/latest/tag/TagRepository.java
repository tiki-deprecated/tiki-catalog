/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TagRepository extends JpaRepository<TagDO, Long> {
    List<TagDO> getAllByValueIn(Collection<String> ids);
}

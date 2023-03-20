/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.use;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface UseRepository extends JpaRepository<UseDO, Long> {
    List<UseDO> getAllByUsecaseIn(Collection<String> usecases);
}

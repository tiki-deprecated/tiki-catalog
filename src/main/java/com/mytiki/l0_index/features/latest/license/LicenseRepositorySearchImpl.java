/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.license;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LicenseRepositorySearchImpl implements LicenseRepositorySearch {
    private final EntityManager em;

    public LicenseRepositorySearchImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<LicenseDO> search(LicenseAOReq req, String appId, Long licenseId, Integer maxResults){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<LicenseDO> cq = cb.createQuery(LicenseDO.class);
        Root<LicenseDO> license = cq.from(LicenseDO.class);

        List<Predicate> filters = new ArrayList<>();
        filters.add(cb.equal(license.get("address").get("appId"), appId));

        if(req.getUsers() != null)
            filters.add(license.get("address").get("userId").in(req.getUsers()));

        if(req.getPtrs() != null)
            filters.add(license.get("title").get("ptr").in(req.getPtrs()));

        if(req.getTags() != null)
            filters.add(license.get("title").get("tags").get("value").in(req.getTags()));

        if(req.getUsecases() != null)
            filters.add(license.get("uses").get("usecase").in(req.getUsecases()));

        if(req.getDestinations() != null)
            filters.add(license.get("uses").get("destination").in(req.getDestinations()));

        if(!req.getIncludeAll())
            filters.add(cb.isTrue(license.get("latest")));

        if(licenseId != null)
            filters.add(cb.greaterThan(license.get("id"), licenseId));

        cq.where(filters.toArray(new Predicate[0])).orderBy(cb.asc(license.get("id")));
        TypedQuery<LicenseDO> query = em.createQuery(cq).setMaxResults(maxResults);
        return query.getResultList();
    }
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.app;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    private final AppService service;

    public AppController(AppService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/app/{id}")
    public AppAO getAddress(@PathVariable(name = "id") String id) {
        return service.getApp(id);
    }
}

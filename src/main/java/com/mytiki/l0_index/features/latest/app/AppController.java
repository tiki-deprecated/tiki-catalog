/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.app;

import com.mytiki.spring_rest_api.ApiConstants;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = AppController.PATH_CONTROLLER)
public class AppController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE + "app/{id}";

    private final AppService service;

    public AppController(AppService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public AppAO getAddress(@PathVariable(name = "id") String id) {
        return service.getApp(id);
    }
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.app;

import com.mytiki.l0_index.utilities.Constants;
import com.mytiki.spring_rest_api.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "FIND")
@RestController
@RequestMapping(value = AppController.PATH_CONTROLLER)
public class AppController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE + "app/{id}";

    private final AppService service;

    public AppController(AppService service) {
        this.service = service;
    }

    @Operation(operationId = Constants.PROJECT_DASH_PATH +  "-app-get",
            summary = "Page Addresses", description = "Get a page of addresses given an app id",
            security = @SecurityRequirement(name = "jwt"))
    @RequestMapping(method = RequestMethod.GET)
    public AppAO getAddress(
            @PathVariable(name = "id") String id,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "100", required = false) int size) {
        return service.getApp(id, page, size);
    }
}

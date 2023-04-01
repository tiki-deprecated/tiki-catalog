/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.title;

import com.mytiki.l0_index.utilities.Constants;
import com.mytiki.spring_rest_api.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "")
@RestController
@RequestMapping(value = TitleController.PATH_CONTROLLER)
public class TitleController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE + "title";

    private final TitleService service;

    public TitleController(TitleService service) {
        this.service = service;
    }

    @Operation(operationId = Constants.PROJECT_DASH_PATH +  "-title-get",
            summary = "Get Title",
            description = "Returns a complete Title Record",
            security = @SecurityRequirement(name = "oauth", scopes = "index"))
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public TitleAORsp get(Principal principal, @PathVariable String id) {
        return service.fetch(id, principal.getName());
    }
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.license;

import com.mytiki.l0_index.utilities.Constants;
import com.mytiki.spring_rest_api.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "")
@RestController
@RequestMapping(value = LicenseController.PATH_CONTROLLER)
public class LicenseController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE + "license";

    private final LicenseService service;

    public LicenseController(LicenseService service) {
        this.service = service;
    }

    @Operation(operationId = Constants.PROJECT_DASH_PATH +  "-license-list",
            summary = "List Licenses",
            description = "Retrieve a list of compressed licenses matching one or more filters",
            security = @SecurityRequirement(name = "oauth", scopes = "index"))
    @RequestMapping(method = RequestMethod.POST)
    public LicenseAORspList list(
            Principal principal,
            @RequestParam(value = "page-token", required = false) Long pageToken,
            @RequestParam(value = "max-results", required = false, defaultValue = "100") Integer maxResults,
            @RequestBody LicenseAOReq body) {
        if(maxResults == null) maxResults = 100;
        return service.list(body, principal.getName(), pageToken, maxResults);
    }

    @Operation(operationId = Constants.PROJECT_DASH_PATH +  "-license-get",
            summary = "Get License",
            description = "Returns a complete License Record",
            security = @SecurityRequirement(name = "oauth", scopes = "index"))
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public LicenseAORsp get(Principal principal, @PathVariable String id) {
        return service.fetch(id, principal.getName());
    }

    @Secured("SCOPE_internal:read")
    @Operation(hidden = true)
    @RequestMapping(method = RequestMethod.POST, path = "/{app-id}")
    public LicenseAORspList listAdmin(
            @PathVariable(value = "app-id") String appId,
            @RequestParam(value = "page-token", required = false) Long pageToken,
            @RequestParam(value = "max-results", required = false, defaultValue = "100") Integer maxResults,
            @RequestBody LicenseAOReq body) {
        if(maxResults == null) maxResults = 100;
        return service.list(body, appId, pageToken, maxResults);
    }
}

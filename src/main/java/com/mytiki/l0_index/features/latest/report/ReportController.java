/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.report;

import com.mytiki.l0_index.utilities.Constants;
import com.mytiki.spring_rest_api.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "REPORT")
@RestController
@RequestMapping(value = ReportController.PATH_CONTROLLER)
public class ReportController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE + "report";

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @Operation(operationId = Constants.PROJECT_DASH_PATH +  "-report-post",
            summary = "Report Block", description = "Report a new block creation to the index service",
            security = @SecurityRequirement(name = "l0Storage"))
    @ApiResponse(responseCode = "200")
    @RequestMapping(method = RequestMethod.POST)
    public void post(@RequestBody ReportAO body) {
        service.report(body);
    }
}

/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.report;

import com.mytiki.l0_index.utilities.Constants;
import com.mytiki.spring_rest_api.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void post(@RequestBody ReportAO body) {
        service.report(body);
    }
}

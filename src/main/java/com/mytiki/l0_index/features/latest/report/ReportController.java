/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.report;

import com.mytiki.spring_rest_api.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ReportController.PATH_CONTROLLER)
public class ReportController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE + "report";

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> post(@RequestBody ReportAO body) {
        service.report(body);
        return ResponseEntity.ok().build();
    }
}

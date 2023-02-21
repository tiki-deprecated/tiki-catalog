/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.features.latest.search;

import com.mytiki.l0_index.utilities.Constants;
import com.mytiki.spring_rest_api.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "SEARCH")
@RestController
@RequestMapping(value = SearchController.PATH_CONTROLLER)
public class SearchController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE + "search";

    private final SearchService service;

    public SearchController(SearchService service) {
        this.service = service;
    }

    @Operation(operationId = Constants.PROJECT_DASH_PATH +  "-search-get",
            summary = "Search", description = "Find an app, address, block, or transaction",
            security = @SecurityRequirement(name = "jwt"))
    @RequestMapping(method = RequestMethod.GET)
    public List<SearchAO> get(@RequestParam String id) {
        return service.search(id);
    }
}

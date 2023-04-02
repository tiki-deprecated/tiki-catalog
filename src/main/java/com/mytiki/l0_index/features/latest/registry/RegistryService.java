package com.mytiki.l0_index.features.latest.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;

public class RegistryService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestTemplate client;

    public RegistryService(RestTemplate client) {
        this.client = client;
    }

    public String getId(String address, String appId){
        String path = "/address/" + address + "?app-id=" + appId;
        try {
            ResponseEntity<RegistryAO> response = client.getForEntity(path, RegistryAO.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getId();
            }
        } catch (Exception e) {
            logger.error("Failed to fetch address", e);
        }
        return null;
    }
}

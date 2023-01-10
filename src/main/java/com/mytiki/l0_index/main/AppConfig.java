package com.mytiki.l0_index.main;

import com.mytiki.l0_index.features.FeaturesConfig;
import com.mytiki.l0_index.health.HealthConfig;
import com.mytiki.l0_index.security.SecurityConfig;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.PostConstruct;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.security.Security;
import java.util.Collections;
import java.util.TimeZone;

@Import({
        SecurityConfig.class,
        HealthConfig.class,
        //UtilitiesConfig.class,
        FeaturesConfig.class
})
public class AppConfig {
    @PostConstruct
    void starter() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Security.setProperty("crypto.policy", "unlimited");
        Security.addProvider(new BouncyCastleProvider());
    }

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String appVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title("L0 Index")
                        .description("Index Service")
                        .version(appVersion)
                        .license(new License()
                                .name("MIT")
                                .url("https://github.com/tiki/l0-index/blob/main/LICENSE")))
                .servers(Collections.singletonList(
                        new Server()
                                .url("https://index.l0.mytiki.com")));
    }
}


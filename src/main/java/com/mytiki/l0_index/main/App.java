package com.mytiki.l0_index.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(AppConfig.class)
@SpringBootApplication
public class App {
    public static void main(final String... args) {
        SpringApplication.run(App.class, args);
    }
}

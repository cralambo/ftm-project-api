package org.ftm.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FtmProjectApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FtmProjectApiApplication.class, args);
    }

}

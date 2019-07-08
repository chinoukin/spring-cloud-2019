package com.cyq.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableElasticsearchRepositories(basePackages = {"com.cyq.es"})
public class ElasticsearchApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApiApplication.class, args);
    }

}

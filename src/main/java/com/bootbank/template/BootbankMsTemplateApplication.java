package com.bootbank.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        excludeName = {
                "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
                "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
                "org.liquibase.LiquibaseAutoConfiguration"
        },
        scanBasePackages = "com.bootbank"
)
public class BootbankMsTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootbankMsTemplateApplication.class, args);
    }
}
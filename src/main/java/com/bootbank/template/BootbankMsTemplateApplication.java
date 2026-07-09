package com.bootbank.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BootbankMsTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootbankMsTemplateApplication.class, args);
	}

}

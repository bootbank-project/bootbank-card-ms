package com.bootbank.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BootbankMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootbankMsApplication.class, args);
	}

}

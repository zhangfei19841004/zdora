package com.zf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableScheduling
@ServletComponentScan
@EnableAsync
//@EnableWebMvc
public class ZdoraApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZdoraApplication.class, args);
	}
}

package com.highright.highcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.highright.highcare", exclude = SecurityAutoConfiguration.class)
public class HighcareApplication {
	public static void main(String[] args) {
		// 그룹웨어 어플리케이션 실행
		SpringApplication.run(HighcareApplication.class, args);
	}

}

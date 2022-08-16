package com.example.FinanceDisplay;

import com.example.FinanceDisplay.Service.UserService;
import com.example.FinanceDisplay.model.FinancialInfo;
import com.example.FinanceDisplay.model.Roles;
import com.example.FinanceDisplay.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FinanceDisplayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceDisplayApplication.class, args);
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

}

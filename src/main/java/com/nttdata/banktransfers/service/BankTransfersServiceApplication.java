package com.nttdata.banktransfers.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BankTransfersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankTransfersServiceApplication.class, args);
	}

}

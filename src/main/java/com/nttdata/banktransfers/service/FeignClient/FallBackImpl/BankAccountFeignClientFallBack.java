package com.nttdata.banktransfers.service.FeignClient.FallBackImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nttdata.banktransfers.service.FeignClient.BankAccountFeignClient;
import com.nttdata.banktransfers.service.model.BankAccounts;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class BankAccountFeignClientFallBack implements BankAccountFeignClient{

	@Value("${api.account-service.uri}")
	private String accountService;
	

	public BankAccounts findById(Long idBankAccount) {
		log.info("BankAccountFeignClientFallBack -> " + accountService+"/"+idBankAccount);
		return null;
	}
}

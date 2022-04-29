package com.nttdata.banktransfers.service.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nttdata.banktransfers.service.FeignClient.FallBackImpl.CreditAccountFeignClientFallBack;
import com.nttdata.banktransfers.service.model.CreditAccount;

 

@FeignClient(name = "${api.credit-service.uri}", fallback = CreditAccountFeignClientFallBack.class)
public interface CreditAccountFeignClient {
	@GetMapping("/{idCreditAccount}")
	CreditAccount findById(@PathVariable("idCreditAccount") Long idCreditAccount);
}

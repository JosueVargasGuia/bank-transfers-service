package com.nttdata.banktransfers.service.FeignClient;


import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nttdata.banktransfers.service.FeignClient.FallBackImpl.MovementCreditFeignClientFallBack;
import com.nttdata.banktransfers.service.model.CreditAccount;
import com.nttdata.banktransfers.service.model.MovementCredit;

 

@FeignClient(name = "${api.movementCredit-service.uri}", fallback = MovementCreditFeignClientFallBack.class)
public interface MovementCreditFeignClient {
	@PostMapping("/recordsMovement")
	public  Map<String, Object>  recordsMovement(@RequestBody MovementCredit movementCredit);
	
	@DeleteMapping("/{idMovementCredit}")
	void delete(@PathVariable("idMovementCredit") Long idMovementCredit);
}

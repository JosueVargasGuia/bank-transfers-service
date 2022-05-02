package com.nttdata.banktransfers.service.FeignClient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.nttdata.banktransfers.service.FeignClient.FallBackImpl.MovementAccountFeignClientFallBack;
import com.nttdata.banktransfers.service.model.MovementAccount;

 

@FeignClient(name = "${api.movement-account-service.uri}", fallback = MovementAccountFeignClientFallBack.class)
public interface MovementAccountFeignClient {
	@GetMapping("/recordAccount")
	Map<String, Object> recordsMovement(@RequestBody MovementAccount movementAccount);

	@DeleteMapping("/{idBankAccount}")
	void delete(@PathVariable("idBankAccount") Long idBankAccount);
}

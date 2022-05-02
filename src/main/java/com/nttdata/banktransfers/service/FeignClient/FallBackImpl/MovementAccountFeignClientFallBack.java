package com.nttdata.banktransfers.service.FeignClient.FallBackImpl;

 
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nttdata.banktransfers.service.FeignClient.MovementAccountFeignClient;
import com.nttdata.banktransfers.service.model.MovementAccount;
import lombok.extern.log4j.Log4j2;


@Log4j2
@Component
public class MovementAccountFeignClientFallBack implements MovementAccountFeignClient { 
	@Value("${api.movement-account-service.uri}")
	private String serviceUri;

	public Map<String, Object> recordsMovement(MovementAccount movementAccount) {
		  log.info("MovementAccountFeignClientFallBack:" +serviceUri+"/recordAccount");
		  Map<String, Object> map=new HashMap<>();
		  map.put("mensaje", "El servicio registro de movimiento(BankAccount) no esta disponible.");
		  map.put("status", "error");
		return map;
	}

 
	public void   delete(Long idBankAccount) {
		log.info("MovementAccountFeignClientFallBack:" +serviceUri+"/"+idBankAccount);
		 
	}
	 
 

}

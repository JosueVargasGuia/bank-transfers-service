package com.nttdata.banktransfers.service.FeignClient.FallBackImpl;

 

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nttdata.banktransfers.service.FeignClient.MovementCreditFeignClient;
import com.nttdata.banktransfers.service.model.MovementCredit;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
 

@Log4j2
@Component
public class MovementCreditFeignClientFallBack implements MovementCreditFeignClient {

	@Value("${api.movementCredit-service.uri}")
	private String serviceUri;
	 
	public Map<String, Object> recordsMovement(MovementCredit movementCredit) {
		 log.info("MovementCreditFeignClientFallBack:" +serviceUri+"/recordAccount");
		  Map<String, Object> map=new HashMap<>();
		  map.put("mensaje", "El servicio registro de movimiento(CreditAccount) no esta disponible.");
		  map.put("status", "error");
		return map;
	 
	}

 
	public void delete(Long idMovementCredit) {
		log.info("MovementCreditFeignClientFallBack:" +serviceUri+"/"+idMovementCredit);
	 
	}
 
}

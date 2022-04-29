package com.nttdata.banktransfers.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.banktransfers.service.entity.BankTransfers;
import com.nttdata.banktransfers.service.service.BankTransfersService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/bank-transfer")
public class BankTransfersController {
	@Autowired
	BankTransfersService bankTransfersService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<BankTransfers> findAll() {
		return bankTransfersService.findAll();

	}

	@GetMapping(value = "/{idBankTransfers}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<BankTransfers>> findById(@PathVariable("idBankTransfers") Long idBankTransfers) {
		return bankTransfersService.findById(idBankTransfers)
				.map(_bankTransfers -> ResponseEntity.ok().body(_bankTransfers)).onErrorResume(e -> {
					log.error("Error: " + e.getMessage());
					return Mono.just(ResponseEntity.badRequest().build());
				}).defaultIfEmpty(ResponseEntity.noContent().build());
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<BankTransfers>> save(@RequestBody BankTransfers bankTransfers) {
		return bankTransfersService.save(bankTransfers).map(_bankTransfers -> ResponseEntity.ok().body(_bankTransfers))
				.onErrorResume(e -> {
					log.error("Error:" + e.getMessage());
					return Mono.just(ResponseEntity.badRequest().build());
				});
	}

	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<BankTransfers>> update(@RequestBody BankTransfers bankTransfers) {
		Mono<BankTransfers> objCankTransfers = bankTransfersService.findById(bankTransfers.getIdBankTransfers())
				.flatMap(_act -> {
					log.info("Update: [new] " + bankTransfers + " [Old]: " + _act);
					return bankTransfersService.update(bankTransfers);
				});
		return objCankTransfers.map(_bankTransfers -> {
			log.info("Status: " + HttpStatus.OK);
			return ResponseEntity.ok().body(_bankTransfers);
		}).onErrorResume(e -> {
			log.error("Status: " + HttpStatus.BAD_REQUEST + " Message:  " + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		}).defaultIfEmpty(ResponseEntity.noContent().build());
	}

	@DeleteMapping(value = "/{idBankTransfers}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Void>> delete(@PathVariable("idBankTransfers") Long idBankTransfers) {
		return bankTransfersService.findById(idBankTransfers).flatMap(bankTransfers -> {
			return bankTransfersService.delete(bankTransfers.getIdBankTransfers())
					.then(Mono.just(ResponseEntity.ok().build()));
		});
	}
}

package com.nttdata.banktransfers.service.service;

import java.util.Map;

import com.nttdata.banktransfers.service.entity.BankTransfers;
import com.nttdata.banktransfers.service.model.Transfers;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankTransfersService {
	Flux<BankTransfers> findAll();

	Mono<BankTransfers> findById(Long idBankTransfers);

	Mono<BankTransfers> save(BankTransfers bankTransfers);

	Mono<BankTransfers> update(BankTransfers bankTransfers);

	Mono<Void> delete(Long idBankTransfers);

	Mono<Map<String, Object>> wireTransfer(Transfers transfers);
}

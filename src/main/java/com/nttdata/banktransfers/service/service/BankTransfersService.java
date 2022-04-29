package com.nttdata.banktransfers.service.service;

import com.nttdata.banktransfers.service.entity.BankTransfers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankTransfersService {
	Flux<BankTransfers> findAll();

	Mono<BankTransfers> findById(Long idBankTransfers);

	Mono<BankTransfers> save(BankTransfers bankTransfers);

	Mono<BankTransfers> update(BankTransfers bankTransfers);

	Mono<Void> delete(Long idBankTransfers);
}

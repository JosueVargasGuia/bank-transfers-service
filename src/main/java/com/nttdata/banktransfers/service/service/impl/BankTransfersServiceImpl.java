package com.nttdata.banktransfers.service.service.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.banktransfers.service.FeignClient.TableIdFeignClient;
import com.nttdata.banktransfers.service.entity.BankTransfers;
import com.nttdata.banktransfers.service.repository.BankTransfersRepository;
import com.nttdata.banktransfers.service.service.BankTransfersService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class BankTransfersServiceImpl implements BankTransfersService {
	@Autowired
	BankTransfersRepository transfersRepository;
	@Autowired
	TableIdFeignClient tableIdFeignClient;

	@Override
	public Flux<BankTransfers> findAll() {
		// TODO Auto-generated method stub
		return transfersRepository.findAll();
	}

	@Override
	public Mono<BankTransfers> findById(Long idBankTransfers) {
		// TODO Auto-generated method stub
		return transfersRepository.findById(idBankTransfers);
	}

	@Override
	public Mono<BankTransfers> save(BankTransfers bankTransfers) {
		Long idBankTransfers = tableIdFeignClient.generateKey(BankTransfers.class.getSimpleName());
		log.info("Key:" + idBankTransfers);
		if (idBankTransfers >= 1) {
			bankTransfers.setCreationDate(Calendar.getInstance().getTime());
			bankTransfers.setIdBankTransfers(idBankTransfers);
		} else {
			return Mono
					.error(new InterruptedException("Servicio no disponible:" + BankTransfers.class.getSimpleName()));
		}

		return transfersRepository.insert(bankTransfers);
	}

	@Override
	public Mono<BankTransfers> update(BankTransfers bankTransfers) {
		// TODO Auto-generated method stub
		return transfersRepository.save(bankTransfers);
	}

	@Override
	public Mono<Void> delete(Long idBankTransfers) {
		// TODO Auto-generated method stub
		return transfersRepository.deleteById(idBankTransfers);
	}

}

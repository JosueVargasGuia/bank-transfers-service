package com.nttdata.banktransfers.service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.banktransfers.service.entity.BankTransfers;
@Repository
public interface BankTransfersRepository extends ReactiveMongoRepository<BankTransfers, Long> {

}

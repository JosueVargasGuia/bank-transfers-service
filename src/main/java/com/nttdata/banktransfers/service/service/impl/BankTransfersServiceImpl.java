package com.nttdata.banktransfers.service.service.impl;

import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.tomcat.websocket.TransformationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nttdata.banktransfers.service.FeignClient.BankAccountFeignClient;
import com.nttdata.banktransfers.service.FeignClient.CreditAccountFeignClient;
import com.nttdata.banktransfers.service.FeignClient.MovementAccountFeignClient;
import com.nttdata.banktransfers.service.FeignClient.MovementCreditFeignClient;
import com.nttdata.banktransfers.service.FeignClient.TableIdFeignClient;
import com.nttdata.banktransfers.service.entity.BankTransfers;
import com.nttdata.banktransfers.service.entity.TypeAccount;
import com.nttdata.banktransfers.service.entity.TypeTransfer;
import com.nttdata.banktransfers.service.model.BankAccounts;
import com.nttdata.banktransfers.service.model.CreditAccount;
import com.nttdata.banktransfers.service.model.MovementAccount;
import com.nttdata.banktransfers.service.model.MovementCredit;
import com.nttdata.banktransfers.service.model.Transfers;
import com.nttdata.banktransfers.service.model.TypeMovementAccount;
import com.nttdata.banktransfers.service.model.TypeMovementCredit;
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
	@Autowired
	BankAccountFeignClient bankAccountFeignClient;
	@Autowired
	CreditAccountFeignClient creditAccountFeignClient;
	@Autowired
	MovementAccountFeignClient movementAccountFeignClient;
	@Autowired
	MovementCreditFeignClient movementCreditFeignClient;

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

		Long count = this.findAll().collect(Collectors.counting()).blockOptional().get();
		Long idBankTransfers;
		if (count != null) {
			if (count <= 0) {
				idBankTransfers = Long.valueOf(0);
			} else {
				idBankTransfers = this.findAll()
						.collect(Collectors.maxBy(Comparator.comparing(BankTransfers::getIdBankTransfers)))
						.blockOptional().get().get().getIdBankTransfers();
			}
		} else {
			idBankTransfers = Long.valueOf(0);
		}
		bankTransfers.setIdBankTransfers(idBankTransfers + 1);
		bankTransfers.setCreationDate(Calendar.getInstance().getTime());

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

	/**
	 * Metodo que realiza la transferencia bancarias o interbancarias segun los
	 * parametros ingresados
	 */
	@Override
	public Mono<Map<String, Object>> wireTransfer(Transfers transfers) {
		Map<String, Object> map = new HashMap<>();
		boolean saveTransfer = false;
		BankAccounts originBankAccount = null;
		CreditAccount originCreditAccount = null;
		BankAccounts targetBankAccount = null;
		CreditAccount targetCreditAccount = null;
		boolean origin = false, target = false;
		if (transfers.getTypeTransfer() == TypeTransfer.banking) {

			/**
			 * Logica para identificar que tipo de tranferencias se realizara BankAccounts
			 * to BankAccounts CreditAccount to CreditAccount BankAccounts to CreditAccount
			 * CreditAccount to BankAccounts
			 */
			if (transfers.getOriginAccount().getTypeAccount() == TypeAccount.BankAccounts) {
				originBankAccount = bankAccountFeignClient.findById(transfers.getOriginAccount().getIdAccount());
				if (originBankAccount != null) {
					MovementAccount records = new MovementAccount();
					records.setAmount(transfers.getAmount());
					records.setTypeMovementAccount(TypeMovementAccount.withdrawal);
					records.setIdBankAccount(transfers.getOriginAccount().getIdAccount());
					Map<String, Object> resul = movementAccountFeignClient.recordsMovement(records);
					String status = (String) resul.get("status");
					if (status != null) {
						if (!status.equalsIgnoreCase("success")) {
							origin = false;
							resul.forEach((key, value) -> {
								if (!key.equalsIgnoreCase("success")) {
									map.put(key, value);
								}
							});
						} else {
							transfers.getOriginAccount()
									.setIdMovement(Long.valueOf(resul.get("idMovementAccount").toString()));
							origin = true;
						}
					} else {
						map.put("ServicioCuenta-origin", "Servicio de registro de movimiento de cuenta no disponible");
					}
					log.info("Result 1:" + resul);
				}
			}
			if (transfers.getOriginAccount().getTypeAccount() == TypeAccount.CreditAccount) {
				originCreditAccount = creditAccountFeignClient.findById(transfers.getOriginAccount().getIdAccount());
				if (originCreditAccount != null) {
					MovementCredit records = new MovementCredit();
					records.setTypeMovementCredit(TypeMovementCredit.charge);
					records.setAmount(transfers.getAmount());
					records.setIdCreditAccount(transfers.getOriginAccount().getIdAccount());
					Map<String, Object> resul = movementCreditFeignClient.recordsMovement(records);
					String status = (String) resul.get("status");
					if (status != null) {
						if (!status.equalsIgnoreCase("success")) {
							resul.forEach((key, value) -> {
								if (!key.equalsIgnoreCase("success")) {
									map.put(key, value);
								}
							});
							origin = false;
						} else {
							transfers.getOriginAccount()
									.setIdMovement(Long.valueOf(resul.get("idMovementCredit").toString()));
							origin = true;
						}
					} else {
						map.put("ServicioCredito-origin",
								"Servicio de registro de movimiento de credito no disponible");
					}
					log.info("Result 2:" + resul);
				}
			}
			if (transfers.getTargetAccount().getTypeAccount() == TypeAccount.BankAccounts) {
				targetBankAccount = bankAccountFeignClient.findById(transfers.getTargetAccount().getIdAccount());
				if (targetBankAccount != null) {
					MovementAccount records = new MovementAccount();
					records.setAmount(transfers.getAmount());
					records.setTypeMovementAccount(TypeMovementAccount.deposit);
					records.setIdBankAccount(transfers.getTargetAccount().getIdAccount());
					Map<String, Object> resul = movementAccountFeignClient.recordsMovement(records);
					String status = (String) resul.get("status");
					if (status != null) {
						if (!status.equalsIgnoreCase("success")) {
							resul.forEach((key, value) -> {
								if (!key.equalsIgnoreCase("success")) {
									map.put(key, value);
								}
							});
							target = false;
						} else {
							transfers.getTargetAccount()
									.setIdMovement(Long.valueOf(resul.get("idMovementAccount").toString()));
							target = true;
						}
					} else {
						map.put("ServicioCuenta-target", "Servicio de registro de movimiento de cuenta no disponible");
					}
					log.info("Result 3:" + resul);
				}
			}
			if (transfers.getTargetAccount().getTypeAccount() == TypeAccount.CreditAccount) {
				targetCreditAccount = creditAccountFeignClient.findById(transfers.getTargetAccount().getIdAccount());
				if (targetCreditAccount != null) {
					MovementCredit records = new MovementCredit();
					records.setTypeMovementCredit(TypeMovementCredit.payment);
					records.setAmount(transfers.getAmount());
					records.setIdCreditAccount(transfers.getTargetAccount().getIdAccount());
					Map<String, Object> resul = movementCreditFeignClient.recordsMovement(records);
					String status = (String) resul.get("status");
					if (status != null) {
						if (!status.equalsIgnoreCase("success")) {
							resul.forEach((key, value) -> {
								if (!key.equalsIgnoreCase("success")) {
									map.put(key, value);
								}
							});
							target = false;
						} else {
							transfers.getTargetAccount()
									.setIdMovement(Long.valueOf(resul.get("idMovementCredit").toString()));
							target = true;
						}
					} else {
						map.put("ServicioCredito-target",
								"Servicio de registro de movimiento de credito no disponible");
					}

					log.info("Result 4:" + resul);
				}
			}
			// log.info("transfers:" + transfers);
			if (origin && target) {
				saveTransfer = true;
			} else {
				map.put("status", "error");
				if (origin = false) {
					map.put("OriginAccount", "La cuenta origen no existe.");
				} else {
					/** Logica para eliminar recordsMovement si existe un error en el origen */
					if (transfers.getOriginAccount().getTypeAccount() == TypeAccount.CreditAccount
							&& transfers.getOriginAccount().getIdMovement() != null) {
						movementCreditFeignClient.delete(transfers.getOriginAccount().getIdMovement());
						map.put("Rollback-Credit-origin",
								"delete CreditAccount para " + transfers.getOriginAccount().getIdMovement());
					}
					if (transfers.getOriginAccount().getTypeAccount() == TypeAccount.BankAccounts
							&& transfers.getOriginAccount().getIdMovement() != null) {
						movementAccountFeignClient.delete(transfers.getOriginAccount().getIdMovement());
						map.put("Rollback-Account-origin",
								"delete BankAccounts para " + transfers.getOriginAccount().getIdMovement());
					}
				}
				if (target = false) {
					map.put("TargetAccount", "La cuenta destino no existe.");
				} else {
					/** Logica para eliminar recordsMovement si existe un error en el destino */
					if (transfers.getTargetAccount().getTypeAccount() == TypeAccount.CreditAccount
							&& transfers.getTargetAccount().getIdMovement() != null) {
						movementCreditFeignClient.delete(transfers.getTargetAccount().getIdMovement());
						map.put("Rollback-Credit-target",
								"delete CreditAccount para " + transfers.getTargetAccount().getIdMovement());
					}
					if (transfers.getTargetAccount().getTypeAccount() == TypeAccount.BankAccounts
							&& transfers.getTargetAccount().getIdMovement() != null) {
						map.put("Rollback-Account-target",
								"delete BankAccounts para " + transfers.getTargetAccount().getIdMovement());
						movementAccountFeignClient.delete(transfers.getTargetAccount().getIdMovement());
					}
				}
			}

		}
		if (transfers.getTypeTransfer() == TypeTransfer.interbank) {
			if (transfers.getInterbankAccountCode() != null) {
				if (transfers.getOriginAccount().getTypeAccount() == TypeAccount.BankAccounts) {
					targetBankAccount = bankAccountFeignClient.findById(transfers.getOriginAccount().getIdAccount());
					if (targetBankAccount != null) {
						MovementAccount records = new MovementAccount();
						records.setAmount(transfers.getAmount());
						records.setTypeMovementAccount(TypeMovementAccount.withdrawal);
						records.setIdBankAccount(transfers.getOriginAccount().getIdAccount());
						Map<String, Object> resul = movementAccountFeignClient.recordsMovement(records);
						String status = (String) resul.get("status");
						if (status != null) {
							log.info("resul:" + resul);
							if (!status.equalsIgnoreCase("success")) {
								resul.forEach((key, value) -> {
									if (!key.equalsIgnoreCase("success")) {
										map.put(key, value);
									}
								});
								origin = false;
							} else {
								transfers.getOriginAccount()
										.setIdMovement(Long.valueOf(resul.get("idMovementAccount").toString()));
								origin = true;
							}
						} else {
							map.put("ServicioCuenta-origin",
									"Servicio de registro de movimiento de cuenta no disponible");
						}
						log.info("Result 5:" + resul);
					}
				}
				if (transfers.getOriginAccount().getTypeAccount() == TypeAccount.CreditAccount) {
					targetCreditAccount = creditAccountFeignClient
							.findById(transfers.getOriginAccount().getIdAccount());
					if (targetCreditAccount != null) {
						MovementCredit records = new MovementCredit();
						records.setTypeMovementCredit(TypeMovementCredit.charge);
						records.setAmount(transfers.getAmount());
						records.setIdCreditAccount(transfers.getOriginAccount().getIdAccount());
						Map<String, Object> resul = movementCreditFeignClient.recordsMovement(records);
						String status = (String) resul.get("status");
						if (status != null) {
							log.info("resul:" + resul);
							if (!status.equalsIgnoreCase("success")) {
								resul.forEach((key, value) -> {
									if (!key.equalsIgnoreCase("success")) {
										map.put(key, value);
									}
								});
								origin = false;
							} else {
								transfers.getOriginAccount()
										.setIdMovement(Long.valueOf(resul.get("idMovementCredit").toString()));
								origin = true;
							}
						} else {
							map.put("ServicioCredito-origin",
									"Servicio de registro de movimiento de credito no disponible");
						}
						log.info("Result 6:" + resul);
					}
				}
			} else {
				map.put("interbankAccountCode", "Es necesario ingresar el codigo interbancario.");
			}

			if (origin) {
				saveTransfer = true;
			} else {
				map.put("status", "error");
			}
		}
		if (saveTransfer) {
			BankTransfers bankTransfers = new BankTransfers();
			bankTransfers.setOriginAccount(transfers.getOriginAccount());
			if (transfers.getOriginAccount().getTypeAccount() == TypeAccount.BankAccounts) {
				bankTransfers.setTargetAccount(transfers.getTargetAccount());
			}
			bankTransfers.setTypeTransfer(transfers.getTypeTransfer());
			bankTransfers.setAmount(transfers.getAmount());
			bankTransfers.setInterbankAccountCode(transfers.getInterbankAccountCode());
			bankTransfers.setOperationDate(Calendar.getInstance().getTime());
			this.save(bankTransfers).subscribe();
			map.put("Transfers", transfers);
			map.put("status", "success");
			map.put("idBankTransfers", bankTransfers.getIdBankTransfers());
		}

		return Mono.just(map);
	}

}

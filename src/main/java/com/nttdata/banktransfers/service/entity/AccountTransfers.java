package com.nttdata.banktransfers.service.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AccountTransfers {
	TypeAccount typeAccount;//BankAccounts o CreditAccount
	private Long idAccount;// Identificador de cuenta que toma los id de BankAccounts.idBankAccount o CreditAccount.idCreditAccount	
	private Long idMovement;//Identificador de movimiento MovementCredit.idMovementCredit o MovementAccount.idBankAccount
}

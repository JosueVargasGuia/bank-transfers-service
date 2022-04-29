package com.nttdata.banktransfers.service.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
 //@Document(collection = "BankAccounts")
 public final class BankAccounts extends Account{
	//@Id
	private Long idBankAccount;
	private Long idProduct;
	//private Long idAccount;
}

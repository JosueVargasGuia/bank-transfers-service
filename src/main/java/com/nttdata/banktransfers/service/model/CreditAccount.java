package com.nttdata.banktransfers.service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Document(collection = "CreditAccount")
public final class CreditAccount extends Account {
	//@Id
	private Long idCreditAccount;
	private Long idProducto;
	private Double amountCreditLimit;
	//private Long idAccount;
}

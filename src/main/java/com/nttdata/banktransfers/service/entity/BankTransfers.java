package com.nttdata.banktransfers.service.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "bank-transfers")
public class BankTransfers {
	@Id
	private Long idBankTransfers;
	private Long idBankAccount;
	private Long idCreditAccount;
	private TypeTransfer typeTransfer;
	private Double amount;
	private String accountNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss") 
	private Date operationDate;
	private String interbankAccountCode;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss") 
	private Date creationDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private Date dateModified;
}

package com.nttdata.banktransfers.service.model;

import com.nttdata.banktransfers.service.entity.AccountTransfers;
import com.nttdata.banktransfers.service.entity.TypeAccount;
import com.nttdata.banktransfers.service.entity.TypeTransfer;

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
public class Transfers {
	TypeTransfer typeTransfer;
	AccountTransfers originAccount;
	AccountTransfers targetAccount;
	Double amount;
	String interbankAccountCode;
}

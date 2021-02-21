package com.bambora.trustlyintegration.model;

import java.text.NumberFormat;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepositForm {
	private String endUserId;
	private String messageId;
	private String name;
	private String lastName;
	private String email;
	private String locale;
	private String amount;
	private String mobilePhone;
	private String nationalIdentificationNumber;
	
	public DepositForm() {
		//Dummie data for testing
		endUserId = UUID.randomUUID().toString();
		messageId = UUID.randomUUID().toString();
		name = "Jorge";
		lastName = "Villalobos";
		email = "jorge.villalobos.b@gmail.com";
		locale = "sv_SE";
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);
		amount = nf.format((Math.random() * 1000 + 1));
		mobilePhone = "123456789";
		nationalIdentificationNumber = "891212-4545";
	}
		
}

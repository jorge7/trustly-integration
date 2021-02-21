package com.bambora.trustlyintegration.service;

import java.util.Map;

import com.bambora.trustlyintegration.model.DepositForm;
import com.bambora.trustlyintegration.model.Order;

public interface TrustlyService {
	
	public Map<String, String> deposit(DepositForm depositForm);
	
	public String notification(String incomingNotificationJson);

	public void saveOrder(String id, String status);

	public Order findOrderById(String id);
	
	public void deleteOrder(String id);
}

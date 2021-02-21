package com.bambora.trustlyintegration.service.impl;

import static com.bambora.trustlyintegration.util.TrustlyConstants.*;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bambora.trustlyintegration.model.DepositForm;
import com.bambora.trustlyintegration.model.Order;
import com.bambora.trustlyintegration.repository.OrderRepository;
import com.bambora.trustlyintegration.service.TrustlyService;
import com.bambora.trustlyintegration.trustly.NotificationHandler;
import com.bambora.trustlyintegration.trustly.SignedAPI;
import com.bambora.trustlyintegration.trustly.commons.Currency;
import com.bambora.trustlyintegration.trustly.commons.ResponseStatus;
import com.bambora.trustlyintegration.trustly.commons.exceptions.TrustlyAPIException;
import com.bambora.trustlyintegration.trustly.commons.exceptions.TrustlyDataException;
import com.bambora.trustlyintegration.trustly.data.notification.Notification;
import com.bambora.trustlyintegration.trustly.data.request.Request;
import com.bambora.trustlyintegration.trustly.data.response.Response;
import com.bambora.trustlyintegration.trustly.requestbuilders.Deposit;


@Service
public class TrustlyServiceImpl implements TrustlyService {
	
	Logger logger = LoggerFactory.getLogger(TrustlyServiceImpl.class);
	
	@Value("${trustly.notification-url}")
	private String notificationURL;
	
	@Value("${trustly.username}")
	private String username;
	
	@Value("${trustly.password}")
	private String password;
	
	@Value("${trustly.private-key-path}")
	private String privateKeyPath;
	
	@Value("${trustly.test-environment}")
	private boolean testEnvironment;
	
	private OrderRepository orderRepository;

	@Autowired
	public TrustlyServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}
	
	@Override
	public Map<String, String> deposit(DepositForm depositForm) {
		SignedAPI api = new SignedAPI();
		api.init(privateKeyPath, "", username, password, testEnvironment);
		Request depositRequest = new Deposit.Build(notificationURL, 
				depositForm.getEndUserId(), 
				depositForm.getMessageId(), 
				Currency.SEK, 
				depositForm.getName(), 
				depositForm.getLastName(), 
				depositForm.getEmail())
	            .locale(depositForm.getLocale())
	            .amount(depositForm.getAmount())
	            .mobilePhone(depositForm.getMobilePhone())
	            .nationalIdentificationNumber(depositForm.getNationalIdentificationNumber())
	            .getRequest();
		
		Map<String, String> responseMap = new HashMap<>();
		
		try {
			Response response = api.sendRequest(depositRequest);
			if (response.getError() == null) {
		    	Map<String, String> mapData = (Map<String, String>) response.getResult().getData();
		   		 
		    	//if deposit hasn't errors, we save orderid with pending status in redis
		   		String orderId = mapData.get(ORDERID);
		   		responseMap.put(ORDERID, mapData.get(ORDERID));
		   		responseMap.put(URL, mapData.get(URL));
		   		
		    	saveOrder(orderId, STATUS_PENDING);
		    } else {
		    	logger.error("Error calling trustly api.");
		    	throw new TrustlyAPIException();
		    }
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	    
		return responseMap;
	}

	@Override
	public String notification(String incomingNotificationJson) {
		SignedAPI api = new SignedAPI();
		api.init(privateKeyPath, "", username, password, testEnvironment);
		
		String responseJson = "";
		try {
			NotificationHandler handler = new NotificationHandler();
		    Notification notification = handler.handleNotification(incomingNotificationJson);
		    Response notificationResponse = handler.prepareNotificationResponse(notification.getMethod(), notification.getUUID(), ResponseStatus.OK);
		    responseJson = handler.toJson(notificationResponse);
		    
		    Order order = orderRepository.findOrderById(notification.getParams().getData().getOrderId());
		    //if order exists, then we update status of payment to OK
		    if (order != null) {
		    	order.setStatus(STATUS_OK);
		    	orderRepository.save(order);
		    } else {
		    	throw new TrustlyDataException();
		    }
		    
		    //TODO: if status is OK, then user's account balance should be credited (increased).
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	    
		return responseJson;
	}

	@Override
	public void saveOrder(String id, String status) {
		orderRepository.save(new Order(id, status));
	}
	
	@Override
	public Order findOrderById(String id) {
		return orderRepository.findOrderById(id);
	}

	@Override
	public void deleteOrder(String id) {
		orderRepository.deleteOrder(id);
	}
}

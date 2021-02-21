package com.bambora.trustlyintegration.controller;

import static com.bambora.trustlyintegration.util.TrustlyConstants.*;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bambora.trustlyintegration.model.Order;
import com.bambora.trustlyintegration.service.TrustlyService;

@RestController
@RequestMapping("/api")
public class TrustlyRestController {
	
	Logger logger = LoggerFactory.getLogger(TrustlyRestController.class);

	private TrustlyService trustlyService;
	
	@Autowired
	public TrustlyRestController(TrustlyService trustlyService) {
		this.trustlyService = trustlyService;
	}
	
	@PostMapping("/notification")
	public String notification(HttpEntity<String> httpEntity) {		
		logger.info("Incoming notification.");
		return trustlyService.notification(httpEntity.getBody());
	}
	
	@GetMapping("/get-status-order/{orderId}")
	public Order redistest(@PathVariable String orderId, HttpServletResponse response) {		
		Order order = trustlyService.findOrderById(orderId);
		if (order.getStatus() != null &&
				(order.getStatus().equals(STATUS_OK) || order.getStatus().equals(STATUS_ERROR))) {
			logger.info("Notification received, proceed to close payment.");
			trustlyService.deleteOrder(orderId); //if success, order is deleted just for free limited space in redis.
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
		} else {
			response.setStatus(HttpServletResponse.SC_PAYMENT_REQUIRED);
		}
		return order;
	}
	
}

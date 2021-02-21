package com.bambora.trustlyintegration.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Order")
public class Order implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -14815274745699077L;
	@Id
	private String id;
	private String status;
}

package com.bambora.trustlyintegration.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.bambora.trustlyintegration.model.Order;

@Repository
public class OrderRepository {
	public static final String HASH_KEY = "Order";
	
    @Autowired
    private RedisTemplate<String, String> template;

    public Order save(Order order){
        template.opsForHash().put(HASH_KEY, order.getId(), order.getStatus());
        return order; 
    }

    public Order findOrderById(String id){
        return new Order(id, (String) template.opsForHash().get(HASH_KEY, id));
    }
    
    public void deleteOrder(String id){
    	template.opsForHash().delete(HASH_KEY, id);
    }
}

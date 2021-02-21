package com.bambora.trustlyintegration.controller;

import static com.bambora.trustlyintegration.util.TrustlyConstants.*;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.bambora.trustlyintegration.model.DepositForm;
import com.bambora.trustlyintegration.service.TrustlyService;


@Controller
public class TrustlyController {

	Logger logger = LoggerFactory.getLogger(TrustlyController.class);

	private TrustlyService trustlyService;
	
	@Autowired
	public TrustlyController(TrustlyService trustlyService) {
		this.trustlyService = trustlyService;
	}

    @GetMapping({"/", "/deposit"})
    public String deposit(Model model) {
    	//Form loaded with random values
    	model.addAttribute("depositForm", new DepositForm());
    	model.addAttribute("view", "deposit");
        return "index";
    }
    
    
    @PostMapping("/deposit")
    public String depositPost(Model model, @ModelAttribute("deposit")DepositForm depositForm) {
    	Map<String, String> responseMap = trustlyService.deposit(depositForm);    	
    	model.addAttribute(ORDERID, responseMap.get(ORDERID));
    	model.addAttribute(URL, responseMap.get(URL));
    	model.addAttribute("view", "waiting-deposit");
		return "index";
    }
    
    @GetMapping("/get-fragment/{type}")
    public String depositPost(Model model, @PathVariable String type) {
    	if (type.equals("success")) {
    		return "fragments::success";
    	} else {
    		return "fragments::failure";
    	}	
    }
    
    
}
package com.fitnesspoint.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fitnesspoint.services.CreditService;

@Controller
@RequestMapping("/credit")
public class CreditController {

	@Autowired
	private CreditService crServ;

	/* --- URLs --- */

	@GetMapping("/purchase")
	public String purchase() {
		return "credit/pruchase.html";
	}

	/* --- SERVICE COM --- */

	@PostMapping("/buy")
	public String buy(Model model, @RequestParam String email, @RequestParam int option) {

		try {

			crServ.buy(email, option);

		} catch (Exception e) {

			System.err.println("Error al comprar créditos. " + e.getMessage());
			
			model.addAttribute("error", e.getMessage());
			
			return "error.html";
			
		}
		
		model.addAttribute("********", email);
		
		return "*";

	}

}
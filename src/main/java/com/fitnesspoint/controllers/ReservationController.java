package com.fitnesspoint.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fitnesspoint.services.ReservationService;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
	
	@Autowired
	private ReservationService reServ;
	
	/* --- URLs --- */
	
	@GetMapping("/newReserve")
	public String newReserve() {
		return "*";
	}
	
	/* --- SERVICE COM --- */
	
	@PostMapping("/reserve")
	public String reserve(Model model, @RequestParam String email, @RequestParam String dayId, @RequestParam int time) {
		
		try {
			
			reServ.reserve(email, dayId, time);
			
		} catch (Exception e) {
			
			System.err.println("Error al hacer una reservación. " + e.getMessage());
			
			model.addAttribute("error", e.getMessage());
			
			return "error.html";
			
		}
		
		return "*";
		
	}

}

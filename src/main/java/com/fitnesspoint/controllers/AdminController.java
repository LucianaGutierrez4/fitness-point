package com.fitnesspoint.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

	/* --- URLs --- */

	@GetMapping("/home")
	public String home() {
		return "admin/home.html";
	}

	/* --- SERVICE COM --- */

	@GetMapping("/lastMonth")
	public String lastMonth(Model model, @RequestParam String username) {

		return "*";

	}

}

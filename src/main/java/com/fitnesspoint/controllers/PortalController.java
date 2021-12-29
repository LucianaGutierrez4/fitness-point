package com.fitnesspoint.controllers;

import java.time.LocalDate;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fitnesspoint.services.MonthhService;
import com.fitnesspoint.services.UserService;

@Controller
@RequestMapping("/")
public class PortalController {

	@Autowired
	private MonthhService moServ;

	@Autowired
	private UserService usServ;

	@GetMapping("/")
	public String welcome(Model model, HttpSession session) {

		if (LocalDate.now().getDayOfMonth() == 1) {

			try {

				moServ.register();

			} catch (Exception e) {

				System.err.println("Error al revisar el mes. " + e.getMessage());

				model.addAttribute("error", e.getMessage());

				return "error.html";

			}

		}

		return "index.html";
	}

	@GetMapping("/registration")
	public String registration() {
		return "registration.html";
	}

	@GetMapping("/login")
	public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout,
			Model model, HttpSession session) {

		if (error != null) {

			model.addAttribute("error", "Usuario o clave incorrectos");

		}

		if (logout != null) {

			model.addAttribute("logout", "Ha salido correctamente");

		}

		return "login.html";
	}

	/* --- SERVICE COM --- */

	@PostMapping("/register")
	public String register(HttpSession session, Model model, @RequestParam String name, @RequestParam String surname,
			@RequestParam String email, @RequestParam String pass1, @RequestParam String pass2) {

		try {

			usServ.register(name, surname, email, pass1, pass2);

		} catch (Exception e) {

			System.err.println("Error al registrar un usuario nuevo. " + e.getMessage());

			model.addAttribute("error", e.getMessage());

			return "registration.html";

		}
		
		return "index.html";

	}

}

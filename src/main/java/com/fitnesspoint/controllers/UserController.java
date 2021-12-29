package com.fitnesspoint.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fitnesspoint.entities.Usserr;
import com.fitnesspoint.errors.ErrorService;
import com.fitnesspoint.services.CreditService;
import com.fitnesspoint.services.DayService;
import com.fitnesspoint.services.MonthhService;
import com.fitnesspoint.services.ReservationService;
import com.fitnesspoint.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private CreditService crServ;

	@Autowired
	private UserService usServ;

	@Autowired
	private MonthhService moServ;

	@Autowired
	private DayService daServ;

	@Autowired
	private ReservationService reServ;

	/* --- URLs --- */

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping("/home")
	public String home(HttpSession session) {
		
		Usserr u = (Usserr) session.getAttribute("uSession");
		
		try {
			reServ.checkReserv(u.getEmail());
		} catch (ErrorService e) {}
		
		session = setSession(session);
		return "home.html";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping("/modification")
	public String modification(HttpSession session) {
		session = setSession(session);
		return "settings.html";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping("/credits")
	public String credits(HttpSession session) {
		session = setSession(session);
		return "credits.html";
	}

	/* --- SERVICE COM --- */

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@PostMapping("/modify")
	public String modify(Model model, HttpSession session, @RequestParam String pass1, @RequestParam String pass2,
			@RequestParam String pass3) {

		Usserr u = (Usserr) session.getAttribute("uSession");

		try {

			usServ.modify(u.getId(), pass1, pass2, pass3);

		} catch (Exception e) {

			System.err.println("Error al modificar el usuario. " + e.getMessage());

			model.addAttribute("error", e.getMessage());

			session.setAttribute("uSession", u);

			return "settings.html";

		}

		return "success.html";

	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping("/calendar")
	public String calendar(HttpSession session, Model model) {

		List<String[]> dMonth = new ArrayList<>();

		try {

			dMonth = moServ.displayMonth();

			session = setSession(session);

		} catch (ErrorService e) {

			System.err.println("Error al mostrar los días del mes. " + e.getMessage());

			model.addAttribute("error", "Error al mostrar los días del mes.");

			return "error.html";

		}

		model.addAttribute("month", dMonth);

		return "calendar.html";

	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@PostMapping("/reservation")
	public String reservation(HttpSession session, Model model, @RequestParam String dayId) {

		List<String[]> day = new ArrayList<>();
		
		try {

			day = daServ.displayDay(dayId);
			model.addAttribute("day", day);
			
			session = setSession(session);

		} catch (ErrorService e) {

			System.err.println("Error al mostrar el día selecionado. " + e.getMessage());

			model.addAttribute("error", e.getMessage());

			return "error.html";

		}

		return "reservation.html";

	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping("/reservation2")
	public String reservation2(HttpSession session, Model model, @RequestParam int sTime, @RequestParam String dayId) {

		Usserr u = (Usserr) session.getAttribute("uSession");
		int[] credits = (int[]) session.getAttribute("credits");
		int sTime2 = sTime - 1;

		if (credits[0] == 0) {

			return "noCredits.html";

		}

		try {

			reServ.reserve(u.getId(), dayId, sTime2);

		} catch (ErrorService e) {

			System.err.println("Error al reservar. " + e.getMessage());

			model.addAttribute("error", e.getMessage());

			return "error.html";

		}

		return "success.html";

	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping("/confirmation")
	public String confirmation(HttpSession session, Model model, @RequestParam int credits) {

		Usserr u = (Usserr) session.getAttribute("uSession");

		try {

			crServ.buy(u.getEmail(), credits);

		} catch (ErrorService e) {

			System.err.println("Error al comprar créditos. " + e.getMessage());

			model.addAttribute("error", e.getMessage());
			return "error.html";

		}

		session = setSession(session);

		return "success.html";

	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping("/myReservations")
	public String myReservations(HttpSession session, Model model) {

		Usserr u = (Usserr) session.getAttribute("uSession");
		List<String[]> dReservations = new ArrayList<>();

		try {

			dReservations = reServ.displayReservations(u.getEmail());
			model.addAttribute("reservations", dReservations);

		} catch (ErrorService e) {

			System.err.println("Error al mostrar las reservaciones. " + e.getMessage());

			model.addAttribute("error", e.getMessage());

			return "error.html";

		}

		session = setSession(session);

		return "myReservations.html";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping("/cancel")
	public String cancel(HttpSession session, Model model, @RequestParam String reservId) {
		
		try {
			 
			reServ.cancelReserv(reservId);
				
		} catch(ErrorService e) {
			
			System.err.println("Error al cancelar la reservación. " + e.getMessage());
			
			model.addAttribute("error", e.getMessage());
			
			return "error.html";
			
		}
		
		session = setSession(session);
		
		return "success.html";
		
	}

	public HttpSession setSession(HttpSession session) {

		Usserr login = (Usserr) session.getAttribute("uSession");
		
		crServ.checkCredits(login.getEmail());
		
		try {
			reServ.checkReserv(login.getEmail());
		} catch(ErrorService e) {
			
			System.err.println("Error al revisar las reservaciones. " + e.getMessage());
			
		}

		int[] credits = crServ.displayCredits(login.getEmail());
		session.setAttribute("credits", credits);

		return session;

	}

}

package com.fitnesspoint.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitnesspoint.entities.Credit;
import com.fitnesspoint.entities.Usserr;
import com.fitnesspoint.errors.ErrorService;
import com.fitnesspoint.repositories.CreditRepository;

@Service
public class CreditService {

	@Autowired
	private CreditRepository crRepo;

	@Autowired
	private UserService usServ;

	@Transactional
	public void buy(String email, int option) throws ErrorService {
		List<Credit> credits = new ArrayList<>();

		Usserr u = usServ.findByEmail(email);


		for (int i = 0; i < option; i++) {

			credits.add(new Credit(190.0, u, LocalDate.now().plusMonths(2), false, false, LocalDate.now()));

		}

		for (Credit c : credits) {

			crRepo.save(c);

		}

	}

	public Credit showById(String id) throws ErrorService {

		Optional<Credit> response = crRepo.findById(id);

		if (response.isPresent()) {

			Credit c = response.get();

			return c;

		} else {

			throw new ErrorService("No se encontró un crédito con esa ID.");

		}

	}

	@Transactional
	public void checkCredits(String email) {

		List<Credit> uCredits = new ArrayList<>();

		try {

			uCredits = showByUser(email);

		} catch (Exception e) {

			System.err.println(e.getMessage());

		}

		for (Credit c : uCredits) {

			if (((c.getExpirationDate().equals(LocalDate.now()) || (c.getExpirationDate().isBefore(LocalDate.now()))))
					&& (c.getReserved() == false)) {

				c.setDisabled(true);
				crRepo.save(c);

			}

		}

	}

	public List<Credit> showByUser(String email) throws ErrorService {
		List<Credit> uCredits = crRepo.showByUser(email);

		if (uCredits == null || uCredits.size() == 0) {

			throw new ErrorService("No hay créditos que revisar.");

		}
		
		uCredits.removeIf(credit -> (credit.getDisabled() == true));

		return uCredits;

	}

	public List<Credit> showUnreservedByUser(String email) throws ErrorService {
		List<Credit> uCredits = showByUser(email);
		
		uCredits.removeIf(credit-> (credit.getReserved() == true));


		if (uCredits.size() == 0) {

			throw new ErrorService("No hay créditos disponibles.");

		}

		return uCredits;

	}

	public List<Credit> showReservedByUser(String email) throws Exception {
		List<Credit> uCredits = showByUser(email);

		uCredits.removeIf(credit-> (credit.getReserved() == false));

		if (uCredits.size() == 0) {

			throw new Exception("No hay créditos usados.");

		}

		return uCredits;

	}

	@Transactional
	public void newReservation(String creditId) throws ErrorService {

		Credit c = showById(creditId);

		if (c.getReserved() == true) {

			throw new ErrorService("El crédito ya está en uso.");

		}

		c.setReserved(true);
		crRepo.save(c);

	}

	@Transactional
	public void cancelReserv(String creditId) throws ErrorService {

		Credit c = showById(creditId);

		c.setReserved(false);
		crRepo.save(c);

	}

	public int[] displayCredits(String email) {

		int[] creditNums = { 0, 0 };
		List<Credit> uCredits = new ArrayList<>();

		try {

			uCredits = showByUser(email);

		} catch (Exception e) {

			return creditNums;

		}

		for (Credit c : uCredits) {

			if (c.getReserved() == false) {

				creditNums[0] += 1;

			} else {

				creditNums[1] += 1;

			}

		}

		return creditNums;

	}
	
	@Transactional
	public void disableCredit(Credit c) throws ErrorService {
		
		if (c == null) {
			
			throw new ErrorService("No se encontró el crédito que se intenta deshabilitar.");
			
		}
		
		if (crRepo.findAll().contains(c) == false) {
			
			throw new ErrorService("El crédito no está en la base de datos.");
			
		}
		
		c.setDisabled(true);
		crRepo.save(c);
		
	}

}

package com.fitnesspoint.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitnesspoint.comparators.ReservationSorter;
import com.fitnesspoint.entities.Credit;
import com.fitnesspoint.entities.Day;
import com.fitnesspoint.entities.Reservation;
import com.fitnesspoint.entities.Usserr;
import com.fitnesspoint.errors.ErrorService;
import com.fitnesspoint.repositories.ReservationRepository;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository reRepo;

	@Autowired
	private UserService usServ;

	@Autowired
	private DayService daServ;

	@Autowired
	private CreditService crServ;

	@Transactional
	public void reserve(String id, String dayId, int time) throws ErrorService {

		Usserr u = usServ.findById(id);

		List<Credit> unreCredits = crServ.showUnreservedByUser(u.getEmail());
		List<Reservation> uReservations = showByUser(u.getEmail());

		if (time < 0 || time > 3) {

			throw new ErrorService("El turno recibido no es válido.");

		}

		if (unreCredits.size() == 0 || unreCredits == null) {

			throw new ErrorService("El usuario no tiene créditos disponibles para la reservación.");

		}

		Credit c = unreCredits.get(0);
		Day d = daServ.showById(dayId);
		LocalTime[] chosenTime = { d.getStartTimes()[time], d.getEndTimes()[time] };
		LocalDateTime expirationDateTime = LocalDateTime.of(d.getDate(), (d.getEndTimes()[time].minusHours(2)));

		if (d.getCap()[time] == 0) {

			throw new ErrorService("El turno que se intenta reservar está lleno.");

		}

		if (LocalDate.now().isAfter(d.getDate())) {

			throw new ErrorService("El día en el que se intenta reservar ya ha pasado.");

		}

		for (Reservation reservation : uReservations) {

			if (d.getDate().equals(reservation.getDay().getDate())) {

				if (chosenTime[0].equals(reservation.getTime()[0])) {

					throw new ErrorService("Ya has reservado para este día y este turno.");

				}

			}

		}

		daServ.newReservation(dayId, time);
		crServ.newReservation(c.getId());

		Reservation r = new Reservation();

		r.setDay(d);
		r.setTime(chosenTime);
		r.setUser(u);
		r.setCredit(c);
		r.setExpirationDateTime(expirationDateTime);
		r.setDisabled(false);
		r.setStart(LocalDate.now());

		reRepo.save(r);

	}

	public List<Reservation> showByUser(String email) {
		List<Reservation> uReservations = reRepo.showByUser(email);
		
		for (Reservation r : uReservations) {
			
			System.out.println(r.getExpirationDateTime());
			
		}

		uReservations.removeIf(reservation -> (reservation.getDisabled() == true));

		return uReservations;
	}

	@Transactional
	public void checkReserv(String email) throws ErrorService {
		List<Reservation> uReservations = new ArrayList<>();

		uReservations = showByUser(email);

		for (Reservation r : uReservations) {
			
			System.out.println(r.getExpirationDateTime());

			if (r.getExpirationDateTime().isBefore(LocalDateTime.now())) {

				r.setDisabled(true);
				crServ.disableCredit(r.getCredit());
				reRepo.save(r);

			}

		}

	}

	@Transactional
	public void cancelReserv(String reservId) throws ErrorService{

		try {

			Reservation r = showById(reservId);
			daServ.cancelReserv(r.getDay().getId(), r.getTime()[0]);
			crServ.cancelReserv(r.getCredit().getId());
			r.setDisabled(true);
			reRepo.save(r);

		} catch (ErrorService e) {

			System.err.println(e.getMessage());

		}

	}

	public Reservation showById(String id) throws ErrorService {

		Optional<Reservation> response = reRepo.findById(id);

		if (response.isPresent()) {

			Reservation r = response.get();

			if (r.getDisabled() == false) {

				return r;

			} else {

				throw new ErrorService("Esa reservación ya no existe.");

			}

		} else {

			throw new ErrorService("No se encontró una reservación con esa ID.");

		}

	}

	public List<String[]> displayReservations(String email) throws ErrorService {

		List<Reservation> uReservations = showByUser(email);

		if (uReservations == null || uReservations.size() == 0) {

			throw new ErrorService("El usuario no tiene reservaciones.");

		}

		List<String[]> dReservations = new ArrayList<>();

		uReservations.sort(new ReservationSorter());

		Locale esLocale = new Locale("es", "ES");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		String weekDay;
		String fDate;

		for (Reservation r : uReservations) {

			weekDay = r.getDay().getDate().format(DateTimeFormatter.ofPattern("EEEE", esLocale));
			weekDay = weekDay.substring(0, 1).toUpperCase() + weekDay.substring(1);

			fDate = r.getDay().getDate().format(formatter);
			
			System.out.println(r.getTime()[0] + "         " + r.getTime()[1]);

			String[] strReserv = { weekDay, fDate, r.getTime()[0].toString(), r.getTime()[1].toString(), r.getId() };
			dReservations.add(strReserv);

		}

		return dReservations;

	}

}

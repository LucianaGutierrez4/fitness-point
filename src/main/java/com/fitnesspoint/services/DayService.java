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

import com.fitnesspoint.comparators.DaysSorter;
import com.fitnesspoint.entities.Activity;
import com.fitnesspoint.entities.Day;
import com.fitnesspoint.entities.Monthh;
import com.fitnesspoint.errors.ErrorService;
import com.fitnesspoint.repositories.DayRepository;

@Service
public class DayService {

	@Autowired
	private DayRepository daRepo;

	@Autowired
	private ActivityService acServ;

	@Transactional
	public void register(Monthh m, LocalDate date) throws Exception {

		LocalTime[] startTimes = { LocalTime.of(8, 0), LocalTime.of(10, 0), LocalTime.of(14, 0), LocalTime.of(18, 0) };
		LocalTime[] endTimes = { LocalTime.of(10, 0), LocalTime.of(13, 0), LocalTime.of(18, 0), LocalTime.of(22, 0) };
		int[] cap = { 15, 15, 15, 15 };

		Day d = new Day();
		Activity a = acServ.showActivity();

		d.setDate(date);
		d.setStartTimes(startTimes);
		d.setEndTimes(endTimes);
		d.setCap(cap);
		d.setMonthh(m);
		d.setActivity(a);

		daRepo.save(d);

	}

	public List<Day> showAllDays() throws Exception {

		List<Day> allDays = daRepo.findAll();

		if (allDays == null) {

			throw new Exception("No hay días en la base de datos.");

		}

		return allDays;

	}

	public List<Day> showByMonth(String monthh) throws ErrorService {

		List<Day> mDays = daRepo.showByMonthh(monthh);

		if (mDays == null) {

			throw new ErrorService("No hay días registrados para ese mes.");

		} else if (monthh.isBlank() || monthh.isEmpty() || monthh.equals(null)) {

			throw new ErrorService("No hay un mes registrado con esa referencia.");

		}

		return mDays;

	}

	public void showDays(String monthh) {

		List<Day> month = daRepo.showByMonthh(monthh);

		month.sort(new DaysSorter());

		for (Day d : month) {

			System.out.println(d.getDate());

		}

	}

	public Day showById(String id) throws ErrorService {

		Optional<Day> response = daRepo.findById(id);

		if (response.isPresent()) {

			Day d = response.get();

			return d;

		} else {

			throw new ErrorService("No se pudo encontrar un día con esa ID.");

		}

	}

	@Transactional
	public void newReservation(String dayId, int time) throws ErrorService {

		Day d = showById(dayId);

		int[] cap = d.getCap();
		cap[time] -= 1;

		d.setCap(cap);

		daRepo.save(d);

	}

	@Transactional
	public void cancelReserv(String dayId, LocalTime time) throws ErrorService {

		Day d = showById(dayId);
		int cap[] = d.getCap();

		int timeIndex = 0;

		for (int i = 0; i < d.getCap().length; i++) {

			if (time.equals(d.getStartTimes()[i])) {

				timeIndex = i;

			}

		}

		cap[timeIndex] += 1;
		daRepo.save(d);

	}

	public List<String[]> displayDay(String dayId) throws ErrorService {

		Day d = showById(dayId);

		List<String[]> day = new ArrayList<>();
		Locale esLocale = new Locale("es", "ES");
		String weekDay = d.getDate().format(DateTimeFormatter.ofPattern("EEEE", esLocale));
		weekDay = weekDay.substring(0, 1).toUpperCase() + weekDay.substring(1);
		weekDay = weekDay.concat(" " + d.getDate().getDayOfMonth());

		for (int i = 0; i < d.getCap().length; i++) {

			LocalDateTime compare = LocalDateTime.of(LocalDate.now(), d.getEndTimes()[i]);

			if ((LocalDate.now().equals(d.getDate())) == false) {

				String[] arrDay = { weekDay, d.getStartTimes()[i].toString(), d.getEndTimes()[i].toString(),
						d.getCap()[i] + "", d.getId() };

				day.add(arrDay);

			} else if (compare.isAfter(LocalDateTime.now())) {

				String[] arrDay = { weekDay, d.getStartTimes()[i].toString(), d.getEndTimes()[i].toString(),
						d.getCap()[i] + "", d.getId() };

				day.add(arrDay);

			} else {

				if (LocalTime.now().isAfter(LocalTime.of(22, 00))) {

					String[] arrDay = { weekDay, d.getStartTimes()[i].toString(), d.getEndTimes()[i].toString(),
							d.getCap()[i] + "", null };

					day.add(arrDay);

				}

			}

		}

		return day;

	}

}

package com.fitnesspoint.services;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitnesspoint.comparators.DaysSorter;
import com.fitnesspoint.entities.Day;
import com.fitnesspoint.entities.Monthh;
import com.fitnesspoint.errors.ErrorService;
import com.fitnesspoint.repositories.MonthRepository;

@Service
public class MonthhService {

	@Autowired
	private MonthRepository moRepo;

	@Autowired
	private DayService daServ;

	@Transactional
	public void register() throws Exception {

		Month currentMonth = LocalDate.now().getMonth();
		int cYear = LocalDate.now().getYear();
		LocalDate fdom = LocalDate.of(cYear, currentMonth, 1);

		Monthh lMonth = moRepo.findByReference("cMonth");

		if (lMonth != null) {

			if (lMonth.getMonth().equals(LocalDate.now().getMonth())) {

				return;

			}

			lMonth.setReference(currentMonth.minus(1) + " " + cYear);
			moRepo.save(lMonth);
		}

		Monthh m = new Monthh();
		m.setMonth(currentMonth);
		m.setReference("cMonth");

		moRepo.save(m);

		while (fdom.getMonth() == currentMonth) {

			if ((fdom.getDayOfWeek().getValue() != 6) && (fdom.getDayOfWeek().getValue() != 7)) {

				daServ.register(m, fdom);

			}

			fdom = fdom.plusDays(1);

		}

	}

	public List<String[]> displayMonth() throws ErrorService {

		List<Day> month = daServ.showByMonth("cMonth");
		List<String[]> dMonth = new ArrayList<>();

		month.sort(new DaysSorter());

		Locale esLocale = new Locale("es", "ES");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		String weekDay;
		String fDate;

		for (Day d : month) {

			weekDay = d.getDate().format(DateTimeFormatter.ofPattern("EEEE", esLocale));
			weekDay = weekDay.substring(0, 1).toUpperCase() + weekDay.substring(1);

			fDate = d.getDate().format(formatter);

			if (d.getDate().isBefore(LocalDate.now())) {

				String[] strDay = { weekDay, fDate, null };
				dMonth.add(strDay);

			} else {

				int full = 0;

				for (int cap : d.getCap()) {

					if (cap == 0) {

						full = full + 1;

					}

				}

				if (full == 4) {
					
					String[] strDay = { weekDay, fDate, "FULL" };
					dMonth.add(strDay);
					
				} else {
					
					String[] strDay = { weekDay, fDate, d.getId() };
					dMonth.add(strDay);
					
				}

			}

		}

		return dMonth;

	}

}

package com.fitnesspoint.comparators;

import java.util.Comparator;
import com.fitnesspoint.entities.Reservation;

public class ReservationSorter implements Comparator<Reservation> {

	@Override
	public int compare(Reservation o1, Reservation o2) {
		return o1.getDay().getDate().compareTo(o2.getDay().getDate());
	}
	
}

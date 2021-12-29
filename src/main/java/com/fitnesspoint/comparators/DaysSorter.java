package com.fitnesspoint.comparators;

import java.util.Comparator;

import com.fitnesspoint.entities.Day;

public class DaysSorter implements Comparator<Day> {
	
	@Override
	public int compare(Day o1, Day o2) {
		return o1.getDate().compareTo(o2.getDate());
	}

}

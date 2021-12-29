package com.fitnesspoint.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fitnesspoint.entities.Day;

@Repository
public interface DayRepository extends JpaRepository<Day, String> {
	
	@Query("SELECT d FROM Day d WHERE d.monthh.reference LIKE :value")
	public List<Day> showByMonthh(String value);

}

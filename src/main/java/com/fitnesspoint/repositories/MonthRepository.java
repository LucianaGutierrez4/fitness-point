package com.fitnesspoint.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fitnesspoint.entities.Monthh;

@Repository
public interface MonthRepository extends JpaRepository<Monthh, String>{
	
	@Query("SELECT m FROM Monthh m WHERE m.id != 'a'")
	public Monthh findById();
	
	@Query("SELECT m FROM Monthh m WHERE m.reference LIKE :value")
	public Monthh findByReference(String value);


}

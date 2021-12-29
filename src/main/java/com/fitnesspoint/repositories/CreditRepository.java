package com.fitnesspoint.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fitnesspoint.entities.Credit;

@Repository
public interface CreditRepository extends JpaRepository<Credit, String>{
	
	@Query("SELECT c FROM Credit c WHERE c.user.email LIKE :value")
	public List<Credit> showByUser(String value);

}

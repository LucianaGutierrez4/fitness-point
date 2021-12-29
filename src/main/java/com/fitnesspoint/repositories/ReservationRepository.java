package com.fitnesspoint.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fitnesspoint.entities.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String>{
	
	@Query("SELECT r FROM Reservation r WHERE r.user.email LIKE :value")
	public List<Reservation> showByUser(String value);

}

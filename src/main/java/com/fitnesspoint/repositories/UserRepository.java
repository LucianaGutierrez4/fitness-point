package com.fitnesspoint.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fitnesspoint.entities.Usserr;

@Repository
public interface UserRepository extends JpaRepository<Usserr, String> {
	
	@Query("SELECT u FROM Usserr u WHERE u.email LIKE :value")
	public Usserr findByEmail(String value);
	
	@Query("SELECT u.email FROM Usserr u")
	public List<String> showAllEmails();

	@Query("SELECT u FROM Usserr u WHERE u.email LIKE 'Luciano@test.com'")
	public Usserr showAdmin();
	
	@Query("SELECT u FROM Usserr u WHERE u.role LIKE 'ADMIN'")
	public List<Usserr> showAllAdmins();
	
}

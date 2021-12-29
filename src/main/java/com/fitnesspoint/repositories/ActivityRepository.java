package com.fitnesspoint.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fitnesspoint.entities.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, String> {
	
	@Query("SELECT a FROM Activity a WHERE a.name LIKE :value")
	public Activity showActivity(String value);

}

package com.fitnesspoint.entities;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Reservation {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	
	private LocalDate start;
	private LocalDateTime expirationDateTime;
	private LocalTime[] time;
	private Boolean disabled;
	
	@ManyToOne
	private Day day;
	
	@ManyToOne
	private Usserr user;
	
	@OneToOne
	private Credit credit;
	

	
}

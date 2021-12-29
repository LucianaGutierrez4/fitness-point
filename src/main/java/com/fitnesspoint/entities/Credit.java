package com.fitnesspoint.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Credit {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	
	private LocalDate start;
	private LocalDate expirationDate;
	private Boolean reserved;
	private Boolean disabled;
	private Double price;
	
	@OneToOne
	private Usserr user;
	
	public Credit(Double price, Usserr user, LocalDate expirationDate, Boolean reserved, Boolean disabled, LocalDate start) {
		this.price = price;
		this.user = user;
		this.expirationDate = expirationDate;
		this.reserved = reserved;
		this.disabled = disabled;
		this.start = start;
	}

}

package com.fitnesspoint.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitnesspoint.entities.Activity;
import com.fitnesspoint.entities.Usserr;
import com.fitnesspoint.errors.ErrorService;
import com.fitnesspoint.repositories.ActivityRepository;

@Service
public class ActivityService {

	@Autowired
	private ActivityRepository acRepo;

	@Autowired
	private AdminService adServ;

	@Transactional
	public void register() { // SI SE AGREGAN MAS ACTIVIDADES, CAMBIAR ESTE METODO

		Usserr admin = new Usserr();
		try {
			
			admin = adServ.showAdmin();
			
		} catch(Exception e) {
			
			System.err.println("No se encontró un administrador.");
			
		}	

		String name = "Gym";

		Activity act = new Activity();

		act.setName(name);
		act.setAdmin(admin);

		acRepo.save(act);

	}

	public Activity showActivity() throws ErrorService { // SI SE AGREGAN MAS ACTIVIDADES, CAMBIAR ESTE METODO

		Activity a = acRepo.showActivity("Gym");

		if (a == null) {

			throw new ErrorService("No se encontró la actividad solicitada.");

		}

		return a;

	}

}

package com.fitnesspoint.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitnesspoint.entities.Usserr;
import com.fitnesspoint.enums.Role;
import com.fitnesspoint.errors.ErrorService;
import com.fitnesspoint.repositories.UserRepository;

@Service
public class AdminService {
	
	@Autowired
	private UserService usServ;

	@Autowired
	private UserRepository usRepo;

	@Transactional
	public void convert(String id) throws ErrorService { // SI SE AGREGAN MAS ADMINS, CAMBIAR ESTE METODO

		Usserr u = usServ.findById(id);
		
		if (u.getRole() != Role.ADMIN) {
			
			u.setRole(Role.ADMIN);
			
			usRepo.save(u);
			
		} else {
			
			throw new ErrorService("Este usuario ya cuenta con el rol de administrador.");
			
		}

	}

	public Usserr showAdmin() throws ErrorService { // SI SE AGREGAN MAS ADMINS, CAMBIAR ESTE METODO
		Usserr a = usRepo.showAdmin();

		if (a == null) {

			throw new ErrorService("No se encontró un administrador.");

		}

		return a;

	}
	
	public List<Usserr> showAllAdmins() {
		return usRepo.showAllAdmins();
	}

}

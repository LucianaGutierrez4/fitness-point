package com.fitnesspoint.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fitnesspoint.entities.Usserr;
import com.fitnesspoint.enums.Role;
import com.fitnesspoint.errors.ErrorService;
import com.fitnesspoint.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository usRepo;

	@Transactional
	public void register(String name, String surname, String email, String pass1, String pass2) throws ErrorService {

		List<String> emails = showAllEmails();

		if (name == null || name.isBlank() || name.isEmpty()) {

			throw new ErrorService("El nombre no puede estar en blanco.");

		}

		if (surname == null || surname.isBlank() || surname.isEmpty()) {

			throw new ErrorService("El apellido no puede estar en blanco.");

		}

		if (email == null || email.isBlank() || email.isEmpty()) {

			throw new ErrorService("El correo electrónico no puede estar en blanco.");

		} else if (emails.contains(email)) {

			throw new ErrorService("El correo electrónico ingresado ya está asociado a una cuenta.");

		}
		
		if (email.contains("@") == false) {
			
			throw new ErrorService("No es un correo electrónico válido.");
			
		}

		if (pass1 == null || pass1.isBlank() || pass1.isEmpty() || pass2 == null || pass2.isBlank()
				|| pass2.isEmpty()) {

			throw new ErrorService("Ambos campos de contraseñas son requeridos.");

		} else if (pass1.equals(pass2) == false) {

			throw new ErrorService("Las contraseñas no coinciden.");

		}

		Usserr u = new Usserr();

		u.setName(name);
		u.setSurname(surname);
		u.setEmail(email);
		u.setRole(Role.USER);
		u.setStart(LocalDate.now());

		String crypted = new BCryptPasswordEncoder().encode(pass1);
		u.setPassword(crypted);

		usRepo.save(u);

	}

	@Transactional
	public void modify(String id, String pass1, String pass2, String pass3) throws ErrorService {

		Usserr u = findById(id);

		if (pass1.isBlank() || pass1.isEmpty() || pass1.equals(null)) {
			
			throw new ErrorService("Se deben completar todos los campos");
			
		}
		
		if (pass2.isBlank() || pass2.isEmpty() || pass2.equals(null)) {
			
			throw new ErrorService("Se deben completar todos los campos");
			
		}
		
		if (pass3.isBlank() || pass3.isEmpty() || pass3.equals(null)) {
			
			throw new ErrorService("Se deben completar todos los campos");
			
		}
		
		if (new BCryptPasswordEncoder().matches(pass1, u.getPassword()) == false) {
			
			throw new ErrorService("Contraseña incorrecta.");
			
		}
		
		if (pass2.equals(pass3) == false) {
			
			throw new ErrorService("Las contraseñas no coinciden");
			
		}
		
		String crypted = new BCryptPasswordEncoder().encode(pass2);
		u.setPassword(crypted);

		usRepo.save(u);

	}

	public Usserr findByEmail(String email) throws ErrorService {

		Usserr u = usRepo.findByEmail(email);

		if (u == null) {

			throw new ErrorService("No hay un usuario registrado con ese correo electrónico.");

		} else {

			return u;

		}

	}

	public Usserr findById(String id) throws ErrorService {

		Optional<Usserr> response = usRepo.findById(id);

		if (response.isPresent()) {

			Usserr u = response.get();

			return u;

		} else {

			throw new ErrorService("No se encontró un usuario con esa ID.");

		}

	}

	public List<String> showAllEmails() {
		return usRepo.showAllEmails();
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Usserr u = usRepo.findByEmail(email);

		if (u != null) {

			List<GrantedAuthority> permissions = new ArrayList<>();

			GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + u.getRole());
			permissions.add(p1);

			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession session = attr.getRequest().getSession(true);

			session.setAttribute("uSession", u);
			session.setMaxInactiveInterval(14400);
			
			User user = new User(u.getEmail(), u.getPassword(), permissions);

			return user;

		} else {

			return null;

		}

	}

}

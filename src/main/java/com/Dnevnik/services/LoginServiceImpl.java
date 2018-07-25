package com.Dnevnik.services;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.Dnevnik.entities.Korisnik;
import com.Dnevnik.repositories.KorisnikRep;

@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private KorisnikRep kRep;
	
	@Override
	public Korisnik getKorisnikFromToken(){
		try {
			Authentication auth=SecurityContextHolder.getContext().getAuthentication(); 
			String logIme=auth.getName();
			Korisnik korisnik=kRep.findByLogime(logIme);			
			return korisnik;
			
		} catch (NoSuchElementException e) {
			return null;
		} catch (Exception e) {
			return null;
		}	
	}

}

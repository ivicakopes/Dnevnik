package com.Dnevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.Dnevnik.entities.Korisnik;

public interface KorisnikRep extends CrudRepository<Korisnik, Integer> {

		Object findFirstByLogime(String korIme);

		Korisnik findByLogime(String logIme);

}

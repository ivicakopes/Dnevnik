package com.Dnevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.Dnevnik.entities.Predmet;
import com.Dnevnik.entities.Razred;

public interface PredmetRep extends CrudRepository<Predmet, Integer> {

		Object findByNaziv(String naziv);

	
	
}

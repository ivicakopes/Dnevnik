package com.Dnevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.Dnevnik.entities.Vrsta;

public interface VrstaRep extends CrudRepository<Vrsta, Integer> {

	Vrsta findByNaziv(String string);

}

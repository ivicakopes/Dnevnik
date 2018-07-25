package com.Dnevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.Dnevnik.entities.Razred;

public interface RazredRep extends CrudRepository<Razred, Integer> {

	Razred findByRazred(Integer parseInt);

	boolean existsByRazred(int parseInt);

	

}

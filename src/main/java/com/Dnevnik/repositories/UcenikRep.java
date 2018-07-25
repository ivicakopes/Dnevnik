package com.Dnevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.Dnevnik.entities.Roditelj;
import com.Dnevnik.entities.Ucenik;

public interface UcenikRep extends CrudRepository<Ucenik,Integer> {

	Integer findAllByRoditelj(Roditelj roditelj);

	boolean existsByRoditelj(Roditelj roditelj);

	}

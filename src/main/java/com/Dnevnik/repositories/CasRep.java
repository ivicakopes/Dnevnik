package com.Dnevnik.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.Dnevnik.entities.Cas;
import com.Dnevnik.entities.Raspored;

public interface CasRep extends CrudRepository<Cas, Integer> {

	List<Cas> findAllByRaspored(Raspored raspored);

	

}

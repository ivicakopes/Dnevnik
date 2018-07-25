package com.Dnevnik.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.Dnevnik.entities.Licenca;
import com.Dnevnik.entities.Nastavnik;
import com.Dnevnik.entities.Plan;
import com.Dnevnik.entities.Predmet;
import com.Dnevnik.entities.Raspored;

public interface LicencaRep extends CrudRepository<Licenca,Integer> {

	boolean existsByNastavnikAndPlan(Nastavnik nastavnik, Plan plan);

	List<Licenca> findAllByPlan(Plan plan);

	List<Licenca> findAllByNastavnik(Nastavnik nastavnik);

	}

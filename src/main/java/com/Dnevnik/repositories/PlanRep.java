package com.Dnevnik.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.Dnevnik.entities.Plan;
import com.Dnevnik.entities.Predmet;
import com.Dnevnik.entities.Razred;

public interface PlanRep extends CrudRepository<Plan, Integer> {

	boolean existsByRazred(Razred razred);

	boolean existsByPredmetAndRazred(Predmet novi, Razred razred);

	List<Plan> findAllByPredmet(Predmet predmet);

	Plan findByPredmetAndRazred(Predmet predmet, Razred razred);

}

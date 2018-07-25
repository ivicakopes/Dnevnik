package com.Dnevnik.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.Dnevnik.entities.Nastavnik;
import com.Dnevnik.entities.Odelenje;
import com.Dnevnik.entities.Raspored;
import com.Dnevnik.entities.Razred;

public interface OdelenjeRep extends CrudRepository<Odelenje, Integer> {

	boolean existsByRazredAndNaziv(Razred razred, String naziv);

	List<Odelenje> findAllByRaspored(Raspored raspored);

	boolean existsByRazredni(Nastavnik nastavnik);

}

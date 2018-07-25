package com.Dnevnik.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.Dnevnik.entities.Nastavnik;

public interface NastavnikRep extends CrudRepository<Nastavnik, Integer> {

	@Query(value = "select distinct odelenje from dnevnik.raspored r inner join  dnevnik.licenca l inner join dnevnik.plan  inner join dnevnik.predmet p "
			+ "where r.licenca = l.id "
			+ "and l.plan = plan.id "
			+ "and plan.predmet = p.id "
			+ "and nastavnik = ?1 "
			+ ";", nativeQuery = true)
	List<Integer> odelenja(Integer nastavnik);

	@Query(value = "select distinct odelenje from dnevnik.raspored r inner join  dnevnik.licenca l inner join dnevnik.plan  inner join dnevnik.predmet p "
			+ "where r.licenca = l.id "
			+ "and l.plan = plan.id "
			+ "and plan.predmet = p.id "
			+ "and plan.predmet = ?2 "
			+ "and nastavnik = ?1 "			
			+ ";", nativeQuery = true)
	List<Integer> odelenjaPr(Integer id, Integer pr);
	
	
	@Query(value = "select distinct predmet from dnevnik.raspored r inner join  dnevnik.licenca l inner join dnevnik.plan  inner join dnevnik.predmet p "
			+ "where r.licenca = l.id "
			+ "and l.plan = plan.id "
			+ "and plan.predmet = p.id "
			+ "and nastavnik = ?1 "
			+ ";", nativeQuery = true)
	List<Integer> predmeti(Integer id);
	
	@Query(value = "select distinct predmet from dnevnik.raspored r inner join  dnevnik.licenca l inner join dnevnik.plan  inner join dnevnik.predmet p "
			+ "where r.licenca = l.id "
			+ "and l.plan = plan.id "
			+ "and plan.predmet = p.id "
			+ "and r.odelenje = ?2 "
			+ "and nastavnik = ?1 "
			+ ";", nativeQuery = true)
	List<Integer> predmetiOd(Integer id, Integer od);
	
}

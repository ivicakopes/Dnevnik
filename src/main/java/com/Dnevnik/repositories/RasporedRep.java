package com.Dnevnik.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.Dnevnik.entities.Licenca;
import com.Dnevnik.entities.Odelenje;
import com.Dnevnik.entities.Raspored;

public interface RasporedRep extends CrudRepository<Raspored, Integer> {

	boolean existsByOdelenjeAndLicenca(Odelenje odelenje, Licenca licenca);

	List<Raspored> findAllByLicenca(Licenca licenca);

	@Query(value = "select distinct r.id "
			+ "from dnevnik.raspored r "
			+ "inner join  dnevnik.licenca l "
			+ "inner join dnevnik.plan pl "
			+ "inner join dnevnik.predmet p	"
			+ "where r.licenca = l.id "
			+ "and l.plan = pl.id "
			+ "and pl.predmet = p.id "
			+ "and pl.predmet = ?2 "
			+ "and odelenje = ?1 "
			, nativeQuery = true)
	Integer findByOdelenjeAndLicencaPlanPredmet(Integer od, Integer pr);

}

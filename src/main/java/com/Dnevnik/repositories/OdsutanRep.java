package com.Dnevnik.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.Dnevnik.entities.Cas;
import com.Dnevnik.entities.Odsutan;

public interface OdsutanRep extends CrudRepository<Odsutan, Integer> {

	List<Odsutan> findByCas(Cas cas);

}

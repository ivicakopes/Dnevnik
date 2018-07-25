package com.Dnevnik.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import com.Dnevnik.entities.Ocena;
import com.Dnevnik.entities.Raspored;
import com.Dnevnik.entities.Ucenik;

public interface OcenaRep extends CrudRepository<Ocena, Integer> {

	
//	@Query(value =
//			"SELECT o.id FROM dnevnik.ocena o where raspored = ?1;"			
//	, nativeQuery = true)
//	List<Integer> findAllByRaspored(Raspored raspored);
	
	@Query(value =
			"select grupa.ucenik uc,grupa.raspored  ras,avg(o.ocena) prosek from ocena o right outer join "
				+ "(SELECT u.id ucenik,r.id raspored FROM ucenik u ,raspored r where u.odelenje_id = r.odelenje ) grupa "
		+ "on grupa.ucenik = o.ucenik and grupa.raspored = o.raspored "
		+ "and (o.vrsta = 1 or o.vrsta = 2 or o.vrsta = 3 or o.vrsta = 4) "
		+ "and o.polugodiste = ?1 "
		+ "group by uc,ras "
		+ "order by uc,ras"
			
	, nativeQuery = true)
	List<zak> zakljuci(String polugodiste);
	
	
	static interface zak{
		Integer getUc();
		Integer getRas();
		Double getProsek();
	}

	List<Ocena> findByUcenik(Ucenik ucenik);

	List<Ocena> findByRaspored(Raspored ras);

	

	

	
	
}




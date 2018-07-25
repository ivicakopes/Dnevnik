package com.Dnevnik.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Dnevnik.entities.Odelenje;
import com.Dnevnik.entities.Predmet;
import com.Dnevnik.entities.Razred;
import com.Dnevnik.repositories.NastavnikRep;
import com.Dnevnik.repositories.OcenaRep;
import com.Dnevnik.repositories.OdelenjeRep;
import com.Dnevnik.repositories.PredmetRep;
import com.Dnevnik.repositories.RazredRep;

@Service
public class OcenaServiceImpl implements OcenaService {

	@Autowired
	OcenaRep oRep;
	
	@Autowired
	OdelenjeRep odRep;
	
	@Autowired
	private NastavnikRep nRep;
	
	@Autowired
	private PredmetRep pRep;


	@Override
	public String zakljuciOceneNaPolugodistu(String pg) {
		System.out.println("test");
		for (com.Dnevnik.repositories.OcenaRep.zak zz : oRep.zakljuci(pg)) {
		System.out.println(zz.getUc() + " " + zz.getRas() + " " + zz.getProsek());
		}

		return "ok";

	}

	@Override
	public List<Odelenje> odelenja(Integer id) {
		List<Odelenje> odelenja = new ArrayList<>();
		for (Integer odelenje : nRep.odelenja(id)) {
			odelenja.add(odRep.findById(odelenje).get());			
		}
		return odelenja;
	}

	@Override
	public List<Predmet> predmeti(Integer id) {
		List<Predmet> predmeti = new ArrayList<>();
		for (Integer predmet : nRep.predmeti(id)) {
			predmeti.add(pRep.findById(predmet).get());			
		}
		return predmeti;
	}

	@Override
	public List<Odelenje> odelenjaPr(Integer id, Integer pr) {
		List<Odelenje> odelenja = new ArrayList<>();
		for (Integer odelenje : nRep.odelenjaPr(id,pr)) {
			odelenja.add(odRep.findById(odelenje).get());			
		}
		return odelenja;
	}
	
	@Override
	public List<Predmet> predmetiOd(Integer id, Integer od) {
		List<Predmet> predmeti = new ArrayList<>();
		for (Integer predmet : nRep.predmetiOd(id,od)) {
			predmeti.add(pRep.findById(predmet).get());			
		}
		return predmeti;
	}
}

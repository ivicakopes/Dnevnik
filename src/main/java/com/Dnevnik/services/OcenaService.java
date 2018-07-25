package com.Dnevnik.services;

import java.util.List;

import com.Dnevnik.entities.Odelenje;
import com.Dnevnik.entities.Predmet;

public interface OcenaService {

	public String zakljuciOceneNaPolugodistu(String pg);

	public List<Odelenje> odelenja(Integer id);

	public List<Predmet> predmeti(Integer temp);

	public List<Odelenje> odelenjaPr(Integer temp, Integer temp1);

	public List<Predmet> predmetiOd(Integer temp, Integer temp1);
}

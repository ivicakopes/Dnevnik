package com.Dnevnik.services;

import com.Dnevnik.entities.Korisnik;

public interface LoginService {

	Korisnik getKorisnikFromToken();

}

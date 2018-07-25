package com.Dnevnik.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.Dnevnik.controllers.Util.RESTError;
import com.Dnevnik.entities.Licenca;
import com.Dnevnik.repositories.LicencaRep;
import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(path = "/dnevnik/licenca")
@CrossOrigin("http://localhost:4200")
public class LicencaController {
	@Autowired
	private LicencaRep lRep;
	
	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> vratiSveLicence() {
		try {
			List<Licenca> lista = (List<Licenca>) lRep.findAll();
			if (lista == null) // ako nema nijedne vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiLicenca"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(lista, HttpStatus.OK); // vrati listu godina

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> vratiLicencuId(@PathVariable String id) {
		try {
			if (!lRep.existsById(Integer.parseInt(id))) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiLicenca"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(lRep.findById(Integer.parseInt(id)).get(), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> obrisiLicencuId(@PathVariable String id) {
		try {
			if (lRep.existsById(Integer.parseInt(id))) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiLicenca"), HttpStatus.NOT_FOUND);
			Licenca licenca1 = lRep.findById(Integer.parseInt(id)).get();
			if (licenca1.getRaspored().size() != 0)
				return new ResponseEntity<>(new RESTError("zabranjenoBrisanje"), HttpStatus.BAD_REQUEST);
			lRep.delete(licenca1);
			return new ResponseEntity<>(licenca1, HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
}

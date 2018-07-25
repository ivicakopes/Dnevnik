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
import com.Dnevnik.entities.Roditelj;
import com.Dnevnik.repositories.RoditeljRep;
import com.Dnevnik.repositories.UcenikRep;
import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(path = "/dnevnik/roditelj")
@CrossOrigin("http://localhost:4200")
public class RoditeljController {
	@Autowired
	protected RoditeljRep rRep;
	
	@Autowired
	private UcenikRep uRep;
	

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> vratiSveRoditelje() {
		try {
			List<Roditelj> lista = (List<Roditelj>) rRep.findAll();
			if (lista == null) // ako nema nijedne vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiRoditelj"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(lista, HttpStatus.OK); // vrati listu roditelja

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> vratiRoditeljaId(@PathVariable String id) {
		try {
			if (!rRep.existsById(Integer.parseInt(id)))// ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiRoditelj"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(rRep.findById(Integer.parseInt(id)), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> obrisiRoditeljaId(@PathVariable String id) {
		try {
			if (!rRep.existsById(Integer.parseInt(id)))// ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiRoditelj"), HttpStatus.NOT_FOUND);
			Roditelj roditelj = rRep.findById(Integer.parseInt(id)).get();
			if (uRep.existsByRoditelj(roditelj))
				return new ResponseEntity<>(new RESTError("zabranjenoBrisanje"), HttpStatus.BAD_REQUEST);
			rRep.delete(roditelj);
			return new ResponseEntity<>(roditelj, HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
}

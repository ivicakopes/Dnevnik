package com.Dnevnik.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Dnevnik.controllers.Util.RESTError;
import com.Dnevnik.entities.Cas;
import com.Dnevnik.repositories.CasRep;
import com.Dnevnik.repositories.RasporedRep;
import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(path = "/dnevnik/cas")
@CrossOrigin("http://localhost:4200")
public class CasController {

	@Autowired
	private CasRep cRep;
	@Autowired
	private RasporedRep rRep;
	

	@JsonView(Pogled.Nas.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> zapocniCas(@RequestParam String ras) {
		try {
			Cas novi = new Cas();
			if (!rRep.existsById(Integer.parseInt(ras)))// ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiRaspored"), HttpStatus.NOT_FOUND);
			novi.setRaspored(rRep.findById(Integer.parseInt(ras)).get());
			novi.setPocetak(LocalDateTime.now());
			novi.setZavrsetak(LocalDateTime.now().plusMinutes(45));
			return new ResponseEntity<>(cRep.save(novi), HttpStatus.CREATED); // kreiraj cas
		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> vratiSveCasove() {
		try {
			List<Cas> lista = (List<Cas>) cRep.findAll();
			if (lista == null) // ako nema nijedne vrati gresku
				return new ResponseEntity<>(new RESTError("nePostoje"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(lista, HttpStatus.OK); // vrati listu godina

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> vratiCasId(@PathVariable String id) {
		try {
			if (!cRep.existsById(Integer.parseInt(id))) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiCas"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(cRep.findById(Integer.parseInt(id)), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> obrisiCasId(@PathVariable String id) {
		try {
			if (!cRep.existsById(Integer.parseInt(id))) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiCas"), HttpStatus.NOT_FOUND);
			Cas cas = cRep.findById(Integer.parseInt(id)).get();
			if (cas.getUcenici().size() != 0)
				return new ResponseEntity<>(new RESTError("zabranjenoBrisanje"), HttpStatus.BAD_REQUEST);
			cRep.delete(cas);
			return new ResponseEntity<>(cas, HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
}

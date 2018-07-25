package com.Dnevnik.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.Dnevnik.controllers.Util.GreskaValidacije;
import com.Dnevnik.controllers.Util.RESTError;
import com.Dnevnik.dto.RasporedDto;
import com.Dnevnik.entities.Raspored;
import com.Dnevnik.repositories.LicencaRep;
import com.Dnevnik.repositories.OdelenjeRep;
import com.Dnevnik.repositories.RasporedRep;
import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(path = "/dnevnik/raspored")
@CrossOrigin("http://localhost:4200")
public class RasporedController {
	@Autowired
	private OdelenjeRep oRep;

	@Autowired
	private LicencaRep lRep;

	@Autowired
	private RasporedRep rasRep;


	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> DodajRaspored(@Valid @RequestBody RasporedDto raspored, BindingResult result) {
		try {
			if (result.hasErrors()) // ako ima greska validacije
				return new ResponseEntity<>(new GreskaValidacije(result), HttpStatus.BAD_REQUEST);// vrati gresku
			Raspored novi = new Raspored();
			Integer odelenje = Integer.parseInt(raspored.getOdelenje());
			Integer licenca = Integer.parseInt(raspored.getLicenca());
			if (!oRep.existsById(odelenje))
				return new ResponseEntity<>(new RESTError("nePostojiOdelenje"), HttpStatus.NOT_FOUND);
			novi.setOdelenje(oRep.findById(odelenje).get());
			if (!lRep.existsById(licenca))// ako ne postoji vratigresku
				return new ResponseEntity<>(new RESTError("nePostojiLicenca"), HttpStatus.BAD_REQUEST);
			novi.setLicenca(lRep.findById(licenca).get());
			if (rasRep.existsByOdelenjeAndLicenca(novi.getOdelenje(), novi.getLicenca()))
				return new ResponseEntity<>(new RESTError("vecPostojiRaspored"), HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(rasRep.save(novi), HttpStatus.CREATED);

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> vratiSveRasporede() {
		try {
			List<Raspored> lista = (List<Raspored>) rasRep.findAll();
			if (lista == null) // ako nema nijedne vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiRaspored"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(lista, HttpStatus.OK); // vrati listu rasporeda

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> obrisiRasporedId(@PathVariable String id) {
		try {
			if (!rasRep.existsById(Integer.parseInt(id))) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiRaspored"), HttpStatus.NOT_FOUND);
			Raspored raspored = rasRep.findById(Integer.parseInt(id)).get();
			if (raspored.getCasovi().size() != 0 && raspored.getOcene().size() != 0)
				return new ResponseEntity<>(new RESTError("zabranjenoBrisanje"), HttpStatus.BAD_REQUEST);
			rasRep.delete(raspored);
			return new ResponseEntity<>(raspored, HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
}

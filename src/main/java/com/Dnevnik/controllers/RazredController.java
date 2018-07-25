package com.Dnevnik.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.Dnevnik.controllers.Util.RESTError;
import com.Dnevnik.entities.Razred;
import com.Dnevnik.repositories.RazredRep;
import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping(path = "/dnevnik/razred")
public class RazredController {
	@Autowired
	private RazredRep rRep;
	
	@JsonView(Pogled.Nas.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> vratiSveRazrede() {
		try {
			List<Razred> lista = (List<Razred>) rRep.findAll();
			if (lista == null) // ako nema nijedne vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiRazred"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(lista, HttpStatus.OK); // vrati listu razreda

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

}

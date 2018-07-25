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
import com.Dnevnik.dto.OdsutanDto;
import com.Dnevnik.entities.Odsutan;
import com.Dnevnik.enumeration.Status_odsutan;
import com.Dnevnik.repositories.CasRep;
import com.Dnevnik.repositories.OdsutanRep;
import com.Dnevnik.repositories.UcenikRep;
import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(path = "/dnevnik/odsutan")
@CrossOrigin("http://localhost:4200")
public class OdsutanController {

	@Autowired
	private OdsutanRep oRep;

	@Autowired
	private UcenikRep uRep;

	@Autowired
	private CasRep cRep;
		
	@JsonView(Pogled.Nas.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> UpisiOdsutnog(@Valid @RequestBody OdsutanDto odsutan, BindingResult result) {
		try {
			if (result.hasErrors()) // ako ima greska validacije
				return new ResponseEntity<>(new GreskaValidacije(result), HttpStatus.BAD_REQUEST);// vrati gresku
			Odsutan novi = new Odsutan();
			if (!cRep.existsById(Integer.parseInt(odsutan.getCas())))
				return new ResponseEntity<>(new RESTError("nePostojiCas"), HttpStatus.NOT_FOUND);
			novi.setCas(cRep.findById(Integer.parseInt(odsutan.getCas())).get());
			if (!uRep.existsById(Integer.parseInt(odsutan.getUcenik()))) // ako ne postoji ucenik vratigresku
				return new ResponseEntity<>(new RESTError("nePostojiUcenik"), HttpStatus.BAD_REQUEST);
			novi.setUcenik(uRep.findById(Integer.parseInt(odsutan.getUcenik())).get());
			novi.setStatus(Status_odsutan.ZAVEDEN);
			return new ResponseEntity<>(oRep.save(novi), HttpStatus.CREATED);

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> IzmeniOdsutnog(@PathVariable String id, @Valid @RequestBody OdsutanDto odsutanDto,
			BindingResult result) {
		try {
			if (result.hasErrors()) // ako ima greska validacije
				return new ResponseEntity<>(new GreskaValidacije(result), HttpStatus.BAD_REQUEST);// vrati gresku

			Odsutan odsutan = new Odsutan();
			if (!oRep.existsById(Integer.parseInt(id)))
				return new ResponseEntity<>(new RESTError("nePostojiOdsutan"), HttpStatus.BAD_REQUEST);
			odsutan = oRep.findById(Integer.parseInt(id)).get();

			if (odsutanDto.getUcenik() != null)
				if (!uRep.existsById(Integer.parseInt(odsutanDto.getUcenik())))
					return new ResponseEntity<>(new RESTError("nePostojiUcenik"), HttpStatus.BAD_REQUEST);
			odsutan.setUcenik(uRep.findById(Integer.parseInt(odsutanDto.getUcenik())).get());

			if (odsutanDto.getCas() != null)
				if (!cRep.existsById(Integer.parseInt(odsutanDto.getCas())))
					return new ResponseEntity<>(new RESTError("nePostojiCas"), HttpStatus.BAD_REQUEST);
			odsutan.setCas(cRep.findById(Integer.parseInt(odsutanDto.getCas())).get());

			return new ResponseEntity<>(oRep.save(odsutan), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> vratiSveOdsutne() {
		try {
			List<Odsutan> lista = (List<Odsutan>) oRep.findAll();
			if (lista.size() == 0) // ako nema nijedne vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiOdsutan"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(lista, HttpStatus.OK); // vrati listu odsutnih

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> vratiOdsutnogId(@PathVariable String id) {
		try {
			if (!oRep.existsById(Integer.parseInt(id))) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiOdsutan"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(oRep.findById(Integer.parseInt(id)), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> obrisiOdsutnogId(@PathVariable String id) {
		try {
			if (!oRep.existsById(Integer.parseInt(id)))// ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiOdsutan"), HttpStatus.NOT_FOUND);
			Odsutan odsutan = oRep.findById(Integer.parseInt(id)).get();
			oRep.delete(odsutan);
			return new ResponseEntity<>(odsutan, HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
}

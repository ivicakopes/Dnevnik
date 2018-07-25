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
import com.Dnevnik.dto.OdelenjeDto;
import com.Dnevnik.dto.RazredniDto;
import com.Dnevnik.entities.Nastavnik;
import com.Dnevnik.entities.Odelenje;
import com.Dnevnik.entities.Razred;
import com.Dnevnik.repositories.NastavnikRep;
import com.Dnevnik.repositories.OdelenjeRep;
import com.Dnevnik.repositories.RazredRep;
import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(path = "/dnevnik/odelenje")
@CrossOrigin("http://localhost:4200")
public class OdelenjeController {

	@Autowired
	private OdelenjeRep oRep;

	@Autowired
	private RazredRep rRep;

	@Autowired
	private NastavnikRep nRep;
	

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> DodajOdelenje(@Valid @RequestBody OdelenjeDto odelenje, BindingResult result) {
		try {
			if (result.hasErrors()) { // ako ima greska validacije
				return new ResponseEntity<>(new GreskaValidacije(result), HttpStatus.BAD_REQUEST);// vrati gresku
			}
			Odelenje novo = new Odelenje();
			Integer razred = Integer.parseInt(odelenje.getRazred());
			if (!rRep.existsById(razred))
				return new ResponseEntity<>(new RESTError("nePostojiRazred"), HttpStatus.NOT_FOUND);
			novo.setRazred(rRep.findById(razred).get());
			novo.setNaziv(odelenje.getNaziv());
			if (oRep.existsByRazredAndNaziv(novo.getRazred(), novo.getNaziv())) // ako vec postoji vratigresku
				return new ResponseEntity<>(new RESTError("vecPostojiOdelenje"), HttpStatus.BAD_REQUEST);
			novo.setNaziv(odelenje.getNaziv());
			return new ResponseEntity<>(oRep.save(novo), HttpStatus.CREATED);

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> IzmeniOdelenje(@PathVariable String id, @Valid @RequestBody OdelenjeDto odelenjeDto,
			BindingResult result) {
		try {
			if (result.hasErrors()) { // ako ima greska validacije
				return new ResponseEntity<>(new GreskaValidacije(result), HttpStatus.BAD_REQUEST);// vrati gresku
			}
			if (!oRep.existsById(Integer.parseInt(id)))
				return new ResponseEntity<>(new RESTError("nePostojiOdelenje"), HttpStatus.NOT_FOUND);
			Odelenje odelenje = oRep.findById(Integer.parseInt(id)).get();
			Integer razredInt = Integer.parseInt(odelenjeDto.getRazred());
			if (!rRep.existsById(razredInt))
				return new ResponseEntity<>(new RESTError("nePostojiRazred"), HttpStatus.NOT_FOUND);
			Razred razred = rRep.findByRazred(razredInt);
			if (oRep.existsByRazredAndNaziv(razred, odelenjeDto.getNaziv()))
				return new ResponseEntity<>(new RESTError("vecPostojiOdelenje"), HttpStatus.BAD_REQUEST);
			odelenje.setRazred(razred);
			odelenje.setNaziv(odelenjeDto.getNaziv());
			return new ResponseEntity<>(oRep.save(odelenje), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> vratiSvaOdelenja() {
		try {
			List<Odelenje> lista = (List<Odelenje>) oRep.findAll();
			if (lista == null) // ako nema nijedne vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiOdelenje"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(lista, HttpStatus.OK); // vrati listu odelenja

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> vratiOdelenjeId(@PathVariable String id) {
		try {
			if (!oRep.existsById(Integer.parseInt(id)))// ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiOdelenje"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(oRep.findById(Integer.parseInt(id)), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
	
	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.GET, value = "/ucenici/{id}")
	public ResponseEntity<?> vratiUcenikeOdelenjeId(@PathVariable String id) {
		try {
			if (!oRep.existsById(Integer.parseInt(id)))// ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiOdelenje"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(oRep.findById(Integer.parseInt(id)).get().getUcenici(), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/razredni")
	public ResponseEntity<?> DodeliRazrednog(@Valid @RequestBody RazredniDto razredniDto, BindingResult result) {
		try {
			if (result.hasErrors()) // ako ima greska validacije
				return new ResponseEntity<>(new GreskaValidacije(result), HttpStatus.BAD_REQUEST);// vrati gresku
			Integer odelenjeInt = Integer.parseInt(razredniDto.getOdelenjeId());
			Integer nastavnikInt = Integer.parseInt(razredniDto.getNastavnikId());
			if (!oRep.existsById(odelenjeInt))
				return new ResponseEntity<>(new RESTError("nePostojiOdelenje"), HttpStatus.BAD_REQUEST);
			Odelenje odelenje = oRep.findById(odelenjeInt).get();
			if (!nRep.existsById(nastavnikInt))
				return new ResponseEntity<>(new RESTError("nePostojiNastavnik"), HttpStatus.NOT_FOUND);
			Nastavnik nastavnik = nRep.findById(nastavnikInt).get();
			odelenje.setRazredni(nastavnik);
			return new ResponseEntity<>(odelenje, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.GET, value = "/razredni/{id}")
	public ResponseEntity<?> vratiRazrednogZaOdelenje(@PathVariable String id) {
		try {
			if (!oRep.existsById(Integer.parseInt(id)))// ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiOdelenje"), HttpStatus.NOT_FOUND);
			Nastavnik razredni = oRep.findById(Integer.parseInt(id)).get().getRazredni();
			if (razredni == null) //ako nema razrednog u odelenju(null) ,vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiRazredni"), HttpStatus.OK);
			return new ResponseEntity<>(razredni, HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> obrisiOdelenjeId(@PathVariable String id) {
		try {
			if (!oRep.existsById(Integer.parseInt(id)))// ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiOdelenje"), HttpStatus.NOT_FOUND);
			Odelenje odelenje = oRep.findById(Integer.parseInt(id)).get();
			oRep.delete(odelenje);
			return new ResponseEntity<>(odelenje, HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
}

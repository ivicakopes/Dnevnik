package com.Dnevnik.controllers;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.Dnevnik.dto.LicencaDto;
import com.Dnevnik.entities.Licenca;
import com.Dnevnik.entities.Nastavnik;
import com.Dnevnik.repositories.LicencaRep;
import com.Dnevnik.repositories.NastavnikRep;
import com.Dnevnik.repositories.OdelenjeRep;
import com.Dnevnik.repositories.PlanRep;
import com.Dnevnik.security.Pogled;
import com.Dnevnik.services.OcenaService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping(path = "/dnevnik/nastavnik")

public class NastavnikController {

	@Autowired
	private OcenaService oService;
	
	@Autowired
	private NastavnikRep nRep;

	@Autowired
	private PlanRep planRep;

	@Autowired
	private LicencaRep lRep;

	@Autowired
	private OdelenjeRep oRep;
	
	@JsonView(Pogled.Nas.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> vratiSveNastavnike() {
		try {
			List<Nastavnik> lista = (List<Nastavnik>) nRep.findAll();
			if (lista == null) // ako nema nijedne vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiNastavnik"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(lista, HttpStatus.OK); // vrati listu nastavnika

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> vratiNastavnikaId(@PathVariable String id) {
		try {
			if (!nRep.existsById(Integer.parseInt(id))) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiNastavnik"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(nRep.findById(Integer.parseInt(id)), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.POST, value = "/plan")
	public ResponseEntity<?> unesiLicencu(@Valid @RequestBody LicencaDto lic, BindingResult result) {
		try {
			if (result.hasErrors()) // ako ima greska validacije
				return new ResponseEntity<>(new GreskaValidacije(result), HttpStatus.BAD_REQUEST);// vrati gresku
			List<Licenca> listaUnetih = new ArrayList<>();
			List<Licenca> listaPostoje = new ArrayList<>();
			if (!nRep.existsById(Integer.parseInt(lic.getNastavnik()))) // ako ne postoji nastavnik vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiNastavnik"), HttpStatus.NOT_FOUND);
			for (String planStr : lic.getPlanId().split(",")) {
				Licenca nova = new Licenca();
				nova.setNastavnik(nRep.findById(Integer.parseInt(lic.getNastavnik())).get());

				if (planRep.existsById(Integer.parseInt(planStr))) {
					nova.setPlan(planRep.findById(Integer.parseInt(planStr)).get());
					if (lRep.existsByNastavnikAndPlan(nova.getNastavnik(), nova.getPlan()))
						listaPostoje.add(nova); // dodati u listu da postoji, ne snima
					else {
						lRep.save(nova);
						listaUnetih.add(nova);
					}
				}
			}
			HashMap<String, List<Licenca>> mapa = new HashMap<String, List<Licenca>>();
			mapa.put("Dodate licence za nastavnika", listaUnetih);
			mapa.put("Licence za nastavnika koje vec postoje", listaPostoje);
			return new ResponseEntity<>(mapa, HttpStatus.CREATED);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("exception"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> obrisiNastavnikaId(@PathVariable String id) {
		try {
			if (!nRep.existsById(Integer.parseInt(id)))// ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiNastavnik"), HttpStatus.NOT_FOUND);
			Nastavnik nastavnik = nRep.findById(Integer.parseInt(id)).get();
			if (nastavnik.getLicenca().size() != 0 && oRep.existsByRazredni(nastavnik))
				return new ResponseEntity<>(new RESTError("zabranjenoBrisanje"), HttpStatus.BAD_REQUEST);
			nRep.delete(nastavnik);
			return new ResponseEntity<>(nastavnik, HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
	
	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.GET, value = "/odelenja/{id}")
	public ResponseEntity<?> vratiOdelenjaZaNastavnika(@PathVariable String id) {
		try {
			Integer temp = Integer.parseInt(id);
			if (!nRep.existsById(temp)) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiNastavnik"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(oService.odelenja(temp), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
	
	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.GET, value = "/odelenja/{id}/{pr}")
	public ResponseEntity<?> vratiOdelenjaPrZaNastavnika(@PathVariable String id, @PathVariable String pr) {
		try {
			Integer temp = Integer.parseInt(id);
			Integer temp1 = Integer.parseInt(pr);
			if (!nRep.existsById(temp)) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiNastavnik"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(oService.odelenjaPr(temp,temp1), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
	
	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.GET, value = "/predmeti/{id}")
	public ResponseEntity<?> vratiPredmeteZaNastavnika(@PathVariable String id) {
		try {
			Integer temp = Integer.parseInt(id);
			if (!nRep.existsById(temp)) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiNastavnik"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(oService.predmeti(temp), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
	
	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.GET, value = "/predmeti/{id}/{od}")
	public ResponseEntity<?> vratiPredmeteOdZaNastavnika(@PathVariable String id, @PathVariable String od) {
		try {
			Integer temp = Integer.parseInt(id);
			Integer temp1 = Integer.parseInt(od);
			if (!nRep.existsById(temp)) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiNastavnik"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(oService.predmetiOd(temp,temp1), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
}
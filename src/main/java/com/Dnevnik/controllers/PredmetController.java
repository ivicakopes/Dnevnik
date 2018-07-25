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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Dnevnik.controllers.Util.GreskaValidacije;
import com.Dnevnik.controllers.Util.RESTError;
import com.Dnevnik.dto.PredmetDto;
import com.Dnevnik.dto.Predmet_razredDto;
import com.Dnevnik.entities.Plan;
import com.Dnevnik.entities.Predmet;
import com.Dnevnik.entities.Razred;
import com.Dnevnik.repositories.PlanRep;
import com.Dnevnik.repositories.PredmetRep;
import com.Dnevnik.repositories.RazredRep;
import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping(path = "/dnevnik/predmet")
public class PredmetController {

	@Autowired
	private PredmetRep pRep;

	@Autowired
	private PlanRep planRep;

	@Autowired
	private RazredRep rRep;

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> DodajPredmet(@Valid @RequestBody PredmetDto predmet, BindingResult result) {
		try {
			if (result.hasErrors()) { // ako ima greska validacije vrati gresku
				return new ResponseEntity<>(new RESTError(new GreskaValidacije(result).getMessage(), 100),
						HttpStatus.BAD_REQUEST);
			}
			Predmet novi = new Predmet();
			novi.setNaziv(predmet.getNaziv());
			if (pRep.findByNaziv(novi.getNaziv()) != null)// ako postoji vrati gresku
				return new ResponseEntity<>(new RESTError("vecPostojiPredmet"), HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(pRep.save(novi), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("exception"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> IzmeniPredmet(@Valid @PathVariable String id, @RequestBody PredmetDto predmet,
			BindingResult result) {
		try {
			if (result.hasErrors()) { // ako ima greska validacije vrati gresku
				return new ResponseEntity<>(new RESTError(new GreskaValidacije(result).getMessage(), 100),
						HttpStatus.BAD_REQUEST);
			}
			if (!pRep.existsById(Integer.parseInt(id)))// ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiPredmet"), HttpStatus.NOT_FOUND);
			Predmet p = pRep.findById(Integer.parseInt(id)).get();
			p.setNaziv(predmet.getNaziv());
			return new ResponseEntity<>(pRep.save(p), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("exception"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class) // postavlja razred i fond casova za predmet
	@RequestMapping(method = RequestMethod.POST, value = "/razred")
	public ResponseEntity<?> DodajRazredZaPredmet(@Valid @RequestBody Predmet_razredDto predmetRazred,
			BindingResult result) {
		try {
			if (result.hasErrors()) { // ako ima greska validacije vrati gresku
				return new ResponseEntity<>(new GreskaValidacije(result), HttpStatus.BAD_REQUEST);
			}
			System.out.println(predmetRazred.getFond());
			Predmet predmet = new Predmet();
			if (!pRep.existsById(Integer.parseInt(predmetRazred.getId()))) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiPredmet"), HttpStatus.NOT_FOUND);
			predmet = pRep.findById(Integer.parseInt(predmetRazred.getId())).get();

			Integer brojGodina = predmetRazred.getFond().length();// duzina stringa za razred i fond casova
			if (brojGodina != 8) // mora imati 8 karaktera
				return new ResponseEntity<>(new RESTError("razlicitaDuzina"), HttpStatus.BAD_REQUEST);
			// List<Plan> listaUnetih = new ArrayList<>();
			// List<Plan> listaPostoje = new ArrayList<>();
			for (int i = 0; i < 8; i++) {// prodji kroz string znak po znak
				Plan noviPlan = new Plan();
				noviPlan.setPredmet(predmet);
				Razred razred = new Razred();
				// ako ne postoji razred vrati gresku
				if (!rRep.existsById(i + 1))
					return new ResponseEntity<>(new RESTError("nePostojiRazred"), HttpStatus.BAD_REQUEST);
				razred = rRep.findById(i + 1).get();
				noviPlan.setRazred(razred);
				int fond = Integer.parseInt(predmetRazred.getFond().substring(i, i + 1));
				if (planRep.existsByPredmetAndRazred(predmet, razred)) { // ako postoji predmet/razred
					noviPlan = planRep.findByPredmetAndRazred(predmet, razred);
					noviPlan.setFond(fond);
					if (fond == 0) {
						if (noviPlan.getLicenca().isEmpty())
							planRep.delete(noviPlan);
					} else
						planRep.save(noviPlan);
				} else {
					if (fond != 0) {
						noviPlan.setFond(fond);
						planRep.save(noviPlan);
					}
				}
			}
			return new ResponseEntity<>(planRep.findAllByPredmet(predmet), HttpStatus.CREATED);
		} catch (Exception e) {

			return new ResponseEntity<>(new RESTError("exception"), HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * @JsonView(Pogled.Ad.class) // postavlja razred i fond casova za predmet
	 * 
	 * @RequestMapping(method = RequestMethod.POST, value = "/razred2") public
	 * ResponseEntity<?> UpdateRazredZaPredmet(@Valid @RequestBody Predmet_razredDto
	 * predmetRazred, BindingResult result) { try { if (result.hasErrors()) { // ako
	 * ima greska validacije vrati gresku return new ResponseEntity<>(new
	 * GreskaValidacije(result), HttpStatus.BAD_REQUEST); } Predmet predmet = new
	 * Predmet(); if (!pRep.existsById(Integer.parseInt(predmetRazred.getId()))) //
	 * ako ne postoji vrati gresku return new ResponseEntity<>(new
	 * RESTError("nePostojiPredmet"), HttpStatus.NOT_FOUND); predmet =
	 * pRep.findById(Integer.parseInt(predmetRazred.getId())).get();
	 * 
	 * Integer brojGodina = predmetRazred.getRazred().length();// duzina stringa za
	 * razred i fond casova if (brojGodina != predmetRazred.getFond().length()) //
	 * mora biti isti return new ResponseEntity<>(new RESTError("razlicitaDuzina"),
	 * HttpStatus.BAD_REQUEST); List<Plan> listaUnetih = new ArrayList<>();
	 * List<Plan> listaPostoje = new ArrayList<>(); for (Integer i = 0; i <
	 * brojGodina; i++) {// prodji kroz string znak po znak Plan noviPlan = new
	 * Plan(); noviPlan.setPredmet(predmet); Razred razred = new Razred(); // ako ne
	 * postoji razred vrati gresku if
	 * (!rRep.existsByRazred(Integer.parseInt(predmetRazred.getRazred().substring(i,
	 * i + 1)))) return new ResponseEntity<>(new RESTError("nePostojiRazred"),
	 * HttpStatus.BAD_REQUEST); razred =
	 * rRep.findByRazred(Integer.parseInt(predmetRazred.getRazred().substring(i, i +
	 * 1))); noviPlan.setRazred(razred);
	 * noviPlan.setFond(Integer.parseInt(predmetRazred.getFond().substring(i, i +
	 * 1))); if (planRep.existsByPredmetAndRazred(predmet, razred)) // ako postoji
	 * predmet/razred listaPostoje.add(noviPlan); // ne ubacuje se else { // kreiraj
	 * novi plan planRep.save(noviPlan); listaUnetih.add(noviPlan); }
	 * 
	 * } HashMap<String, List<Plan>> mapa = new HashMap<String, List<Plan>>();
	 * mapa.put("dodati razredi za predmet", listaUnetih);
	 * mapa.put("Razredi za predmet koji vec postoje", listaPostoje); return new
	 * ResponseEntity<>(planRep.findAllByPredmet(predmet), HttpStatus.CREATED); }
	 * catch (Exception e) { return new ResponseEntity<>(new RESTError("exception"),
	 * HttpStatus.BAD_REQUEST); } }
	 */
	@JsonView(Pogled.Ad.class) // promeni fond casova
	@RequestMapping(method = RequestMethod.PUT, value = "/fond")
	public ResponseEntity<?> PromeniFondCasova(@Valid @RequestParam("plan") String planStr,
			@RequestParam("fond") String fondStr) {
		try {
			Plan plan = new Plan();
			if (!planRep.existsById(Integer.parseInt(planStr)))
				return new ResponseEntity<>(new RESTError("nePostojiRazred"), HttpStatus.BAD_REQUEST);
			plan = planRep.findById(Integer.parseInt(planStr)).get();
			plan.setFond(Integer.parseInt(fondStr));
			return new ResponseEntity<>(planRep.save(plan), HttpStatus.ACCEPTED);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("exception"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Nas.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> vratiSvePredmete() {
		try {
			List<Predmet> lista = (List<Predmet>) pRep.findAll();
			if (lista == null) // ako nema nijedne vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiRazred"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(lista, HttpStatus.OK); // vrati listu predmeta

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> vratiPredmetId(@PathVariable String id) {
		try {
			if (pRep.existsById(Integer.parseInt(id)))// ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiPredmet"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(pRep.findById(Integer.parseInt(id)), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.GET, value = "/plan/{id}")
	public ResponseEntity<?> vratiPlanoveZaPredmetId(@PathVariable String id) {
		try {
			if (!pRep.existsById(Integer.parseInt(id)))// ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiPredmet"), HttpStatus.NOT_FOUND);
			Predmet predmet = pRep.findById(Integer.parseInt(id)).get();// ako postoji predmet trazi razrede
			return new ResponseEntity<>(predmet.getPlan(), HttpStatus.OK);
		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.GET, value = "/razredi/{id}")
	public ResponseEntity<?> vratiRazredeZaPredmetId(@PathVariable String id) {
		try {
			List<Razred> razred = new ArrayList<Razred>();
			if (!pRep.existsById(Integer.parseInt(id)))// ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiPredmet"), HttpStatus.NOT_FOUND);
			Predmet predmet = pRep.findById(Integer.parseInt(id)).get();// ako postoji predmet trazi razrede
			for (Plan plan : predmet.getPlan()) {
				razred.add(plan.getRazred());
			}
			return new ResponseEntity<>(razred, HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> obrisiPredmetId(@PathVariable String id) {
		try {
			if (!pRep.existsById(Integer.parseInt(id))) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiPredmet"), HttpStatus.NOT_FOUND);
			Predmet predmet = pRep.findById(Integer.parseInt(id)).get();
			if (predmet.getPlan().size() != 0)
				return new ResponseEntity<>(new RESTError("zabranjenoBrisanje"), HttpStatus.BAD_REQUEST);
			pRep.delete(predmet);
			return new ResponseEntity<>(predmet, HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
}

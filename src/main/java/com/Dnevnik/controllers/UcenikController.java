package com.Dnevnik.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
import com.Dnevnik.dto.UcenikDto;
import com.Dnevnik.entities.Odelenje;
import com.Dnevnik.entities.Raspored;
import com.Dnevnik.entities.Razred;
import com.Dnevnik.entities.Roditelj;
import com.Dnevnik.entities.Ucenik;
import com.Dnevnik.repositories.OdelenjeRep;
import com.Dnevnik.repositories.RazredRep;
import com.Dnevnik.repositories.RoditeljRep;
import com.Dnevnik.repositories.UcenikRep;
import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(path = "/dnevnik/ucenik")
@CrossOrigin("http://localhost:4200")
public class UcenikController {
	@Autowired
	protected UcenikRep uRep;

	@Autowired
	private RazredRep razRep;

	@Autowired
	private RoditeljRep rodRep;

	@Autowired
	private OdelenjeRep oRep;
	
	
	
	@JsonView(Pogled.Nas.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> vratiSveUcenike() {
		try {
			
			List<Ucenik> lista = (List<Ucenik>) uRep.findAll();
			if (lista == null) // ako nema nijedne vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiUcenik"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(lista, HttpStatus.OK); // vrati listu ucenika

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
	
	

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> vratiUcenikaId(@PathVariable String id) {
		try {			
			if (!uRep.existsById(Integer.parseInt(id)))// ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiUcenik"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(uRep.findById(Integer.parseInt(id)), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	// mora se proveriti da li postoje: ucenik, razred , odelenje ,roditelj, kao i
	// da li odelenje pripada bas tom razredu (npr odelenje 5.2 moze biti samo u 5
	// razredu)

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> izmeniUcenikaId(@PathVariable String id, @Valid @RequestBody UcenikDto ucenikDto,
			BindingResult result) {
		try {
			if (result.hasErrors()) // ako ima greska validacije
				return new ResponseEntity<>(new GreskaValidacije(result), HttpStatus.BAD_REQUEST);// vrati gresku) 
			Ucenik ucenik = new Ucenik();

			if (!uRep.existsById(Integer.parseInt(id)))
				return new ResponseEntity<>(new RESTError("nePostojiUcenik"), HttpStatus.NOT_FOUND);
			ucenik = uRep.findById(Integer.parseInt(id)).get();

			Razred razred = new Razred();
			Odelenje odelenje = new Odelenje();
			Roditelj roditelj = new Roditelj();

			if (ucenikDto.getRazred() != null) {
				if (!razRep.existsById(Integer.parseInt(ucenikDto.getRazred())))
					return new ResponseEntity<>(new RESTError("nePostojiRazred"), HttpStatus.NOT_FOUND);
				razred = razRep.findById(Integer.parseInt(ucenikDto.getRazred())).get(); // razred iz tabele razreda
			} else
				razred = ucenik.getRazred(); // ako nije prosledjen , razred je trenutni razred od ucenika

			if (ucenikDto.getOdelenje() != null) {
				if (!oRep.existsById(Integer.parseInt(ucenikDto.getOdelenje())))
					return new ResponseEntity<>(new RESTError("nePostojiOdelenje"), HttpStatus.NOT_FOUND);
				odelenje = oRep.findById(Integer.parseInt(ucenikDto.getOdelenje())).get();
			} else
				odelenje = ucenik.getOdelenje();

			if (ucenikDto.getRoditelj() != null) {
				if (!rodRep.existsById(Integer.parseInt(ucenikDto.getRoditelj())))
					return new ResponseEntity<>(new RESTError("nePostojiRoditelj"), HttpStatus.NOT_FOUND);
				roditelj = rodRep.findById(Integer.parseInt(ucenikDto.getRoditelj())).get();
			} else
				roditelj = ucenik.getRoditelj();

			if (odelenje.getRazred() != razred) // odelenje mora pripadati odredjenom razredu
				return new ResponseEntity<>(new RESTError("greskaOdelenjeRazred"), HttpStatus.NOT_FOUND);
			ucenik.setRazred(razred);
			ucenik.setOdelenje(odelenje);
			ucenik.setRoditelj(roditelj);
			return new ResponseEntity<>(uRep.save(ucenik), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Uc.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}/odelenje")
	public ResponseEntity<?> obrisiUcenikaIzOdelenjaId(@PathVariable String id) {
		try {
			Ucenik ucenik = new Ucenik();
			if (!uRep.existsById(Integer.parseInt(id)))
				return new ResponseEntity<>(new RESTError("nePostojiUcenik"), HttpStatus.NOT_FOUND);
			ucenik = uRep.findById(Integer.parseInt(id)).get();
			ucenik.setOdelenje(null);
			return new ResponseEntity<>(uRep.save(ucenik), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> obrisiUcenikaId(@PathVariable String id) {
		try {
			if (!uRep.existsById(Integer.parseInt(id))) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiUcenik"), HttpStatus.NOT_FOUND);
			Ucenik ucenik = uRep.findById(Integer.parseInt(id)).get();
			if (ucenik.getCasovi().size() != 0 || ucenik.getOcena().size() != 0) // ako se koristi u bazi ne moze se brisati
				return new ResponseEntity<>(new RESTError("zabranjenoBrisanje"), HttpStatus.BAD_REQUEST);
			uRep.delete(ucenik);
			return new ResponseEntity<>(ucenik, HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/predmeti")
	public List<String> vratiUcenikePredmet(@Valid @RequestParam Integer uc) {
		List<Raspored> predmeti = uRep.findById(uc).get().getOdelenje().getRaspored();
		List<String> lista = new ArrayList<>();
		lista.add("Spisak predmeta sa Nastavnicima koji ih predaju ");
		for (Raspored ras : predmeti) {
			lista.add(ras.getLicenca().getPlan().getPredmet().getNaziv() + "--"
					+ ras.getLicenca().getNastavnik().getPrezime() + " " + ras.getLicenca().getNastavnik().getIme());
		}
		return lista;
	}
}

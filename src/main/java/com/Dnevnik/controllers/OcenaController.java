package com.Dnevnik.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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

import com.Dnevnik.Models.EmailObject;
import com.Dnevnik.controllers.Util.GreskaValidacije;
import com.Dnevnik.controllers.Util.RESTError;
import com.Dnevnik.dto.OcenaDto;
import com.Dnevnik.dto.OcenaDtoPut;
import com.Dnevnik.entities.Ocena;
import com.Dnevnik.entities.Plan;
import com.Dnevnik.entities.Raspored;
import com.Dnevnik.entities.Ucenik;
import com.Dnevnik.repositories.OcenaRep;
import com.Dnevnik.repositories.PlanRep;
import com.Dnevnik.repositories.RasporedRep;
import com.Dnevnik.repositories.UcenikRep;
import com.Dnevnik.repositories.VrstaRep;
import com.Dnevnik.security.Pogled;
import com.Dnevnik.services.EmailService;
import com.Dnevnik.services.OcenaService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(path = "/dnevnik/ocena")
@CrossOrigin("http://localhost:4200")
public class OcenaController {

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private OcenaService oService;

	@Autowired
	private OcenaRep oRep;
	
	@Autowired
	private PlanRep planRep;

	@Autowired
	private UcenikRep uRep;

	@Autowired
	private RasporedRep rRep;

	@Autowired
	private VrstaRep vRep;
	/*
	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.GET, value = "/svePoPredmetuUcenik/{id}")
	public ResponseEntity<?> vratiSveOcenePoPredmetuZaUcenika(@PathVariable String id) {
		try {
			if (!uRep.existsById(Integer.parseInt(id)))
				return new ResponseEntity<>(new RESTError("nePostojiUcenik"), HttpStatus.BAD_REQUEST);
			Ucenik ucenik = uRep.findById(Integer.parseInt(id)).get();
			List<Ocena> listaSvih = ucenik.getOcena();
			List<Raspored> listaRasporeda = ucenik.getOdelenje().getRaspored();
			List<Ocena> listaOcena = new ArrayList<>();
			for (Raspored raspored : listaRasporeda) {
				
				
			}
			if (listaSvih.isEmpty()) // ako nema nijedne vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiOcena"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(lista, HttpStatus.OK); // vrati listu ocena

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
*/
	@JsonView(Pogled.Nas.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> DodajOcenu(@Valid @RequestBody OcenaDto ocenaDto, BindingResult result) {
		try {
			if (result.hasErrors()) // ako ima greska validacije vrati gresku
				return new ResponseEntity<>(new GreskaValidacije(result), HttpStatus.BAD_REQUEST);
			Ocena nova = new Ocena();

			if (!rRep.existsById(Integer.parseInt(ocenaDto.getRaspored())))
				return new ResponseEntity<>(new RESTError("nePostojiRaspored"), HttpStatus.NOT_FOUND);
			nova.setRaspored(rRep.findById(Integer.parseInt(ocenaDto.getRaspored())).get());

			if (!uRep.existsById(Integer.parseInt(ocenaDto.getUcenik())))
				return new ResponseEntity<>(new RESTError("nePostojiUcenik"), HttpStatus.BAD_REQUEST);
			nova.setUcenik(uRep.findById(Integer.parseInt(ocenaDto.getUcenik())).get());

			if (!nova.getUcenik().getOdelenje().equals(nova.getRaspored().getOdelenje()))
				return new ResponseEntity<>(new RESTError("greskaUcenikOdelenje"), HttpStatus.BAD_REQUEST);
			nova.setPolugodiste(ocenaDto.getPolugodiste());
			nova.setDatum(LocalDate.parse(ocenaDto.getDatum(), dtf));
			nova.setOcena(Integer.parseInt(ocenaDto.getOcena()));
			nova.setOpis(ocenaDto.getOpis());

			switch (ocenaDto.getVrsta()) {
			case "U":
				nova.setVrsta(vRep.findByNaziv("usmeni"));
				break;
			case "K":
				nova.setVrsta(vRep.findByNaziv("kontrolni"));
				break;
			case "P":
				nova.setVrsta(vRep.findByNaziv("pismeni"));
				break;
			case "Z":
				nova.setVrsta(vRep.findByNaziv("zalaganje"));
				break;
			case "R":
				nova.setVrsta(vRep.findByNaziv("rad"));
				break;
			}
			EmailObject mail = new EmailObject();
			mail.setTo(nova.getUcenik().getEmail());
			mail.setSubject("Ocena");
			mail.setText(" Ucenik :" + nova.getUcenik().getIme() + " " + nova.getUcenik().getPrezime() 
					+ " <br>Predmet :" + nova.getRaspored().getLicenca().getPlan().getPredmet().getNaziv() 
					+ " ocena  " 
					+ nova.getOcena() + " Predmet :" + nova.getRaspored().getLicenca().getPlan().getPredmet().getNaziv() );
			emailService.sendTemplateMessage(mail);
			return new ResponseEntity<>(oRep.save(nova), HttpStatus.CREATED);

		} catch (DateTimeParseException e) {
			return new ResponseEntity<>(new RESTError("neispravanDatum"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Nas.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> IzmeniOcenu(@PathVariable String id, @Valid @RequestBody OcenaDtoPut ocenaDtoPut,
			BindingResult result) {
		try {
			if (result.hasErrors()) // ako ima greska validacije vrati gresku
				return new ResponseEntity<>(new GreskaValidacije(result), HttpStatus.BAD_REQUEST);
			Ocena nova = new Ocena();

			if (!oRep.existsById(Integer.parseInt(id)))
				return new ResponseEntity<>(new RESTError("nePostojiOcena"), HttpStatus.BAD_REQUEST);
			nova = oRep.findById(Integer.parseInt(id)).get();

			if (ocenaDtoPut.getRaspored() != null) {
				if (!rRep.existsById(Integer.parseInt(ocenaDtoPut.getRaspored())))
					return new ResponseEntity<>(new RESTError("nePostojiRaspored"), HttpStatus.NOT_FOUND);
				nova.setRaspored(rRep.findById(Integer.parseInt(ocenaDtoPut.getRaspored())).get());
			}
			if (ocenaDtoPut.getUcenik() != null) {
				if (!uRep.existsById(Integer.parseInt(ocenaDtoPut.getUcenik())))
					return new ResponseEntity<>(new RESTError("nePostojiUcenik"), HttpStatus.NOT_FOUND);
				nova.setUcenik(uRep.findById(Integer.parseInt(ocenaDtoPut.getUcenik())).get());
			}
			if (!nova.getUcenik().getOdelenje().equals(nova.getRaspored().getOdelenje()))
				return new ResponseEntity<>(new RESTError("greskaUcenikOdelenje"), HttpStatus.BAD_REQUEST);

			if (ocenaDtoPut.getOcena() != null)
				nova.setOcena(Integer.parseInt(ocenaDtoPut.getOcena()));

			if (ocenaDtoPut.getVrsta() != null)
				switch (ocenaDtoPut.getVrsta()) {
				case "U":
					nova.setVrsta(vRep.findByNaziv("usmeni"));
					break;
				case "K":
					nova.setVrsta(vRep.findByNaziv("kontrolni"));
					break;
				case "P":
					nova.setVrsta(vRep.findByNaziv("pismeni"));
					break;
				case "Z":
					nova.setVrsta(vRep.findByNaziv("zalaganje"));
					break;
				case "R":
					nova.setVrsta(vRep.findByNaziv("rad"));
					break;
				}
			if (ocenaDtoPut.getPolugodiste() != null)
				nova.setPolugodiste(ocenaDtoPut.getPolugodiste());

			if (ocenaDtoPut.getOpis() != null)
				nova.setOpis(ocenaDtoPut.getOpis());

			if (ocenaDtoPut.getDatum() != null)
				nova.setDatum(LocalDate.parse(ocenaDtoPut.getDatum(), dtf));

			return new ResponseEntity<>(oRep.save(nova), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (DateTimeParseException e) {
			return new ResponseEntity<>(new RESTError("neispravanDatum"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> vratiSveOcene() {
		try {
			List<Ocena> lista = (List<Ocena>) oRep.findAll();
			if (lista == null) // ako nema nijedne vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiOcena"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(lista, HttpStatus.OK); // vrati listu ocena

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
	
	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.GET, value = "/ocenaOdPr/{od}/{pr}" )
	public ResponseEntity<?> vratiSveOceneOdPr(@PathVariable Integer od,@PathVariable Integer pr) {
		try {
			Integer rasp = rRep.findByOdelenjeAndLicencaPlanPredmet(od,pr);
			Raspored ras = rRep.findById(rasp).get();
			List<Ocena> lista = oRep.findByRaspored(ras);
			if (lista == null) // ako nema nijedne vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiOcena"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(lista, HttpStatus.OK); // vrati listu ocena
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> vratiOcenuId(@PathVariable String id) {
		try {
			if (!oRep.existsById(Integer.parseInt(id))) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiOcena"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(oRep.findById(Integer.parseInt(id)), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> obrisiOcenuId(@PathVariable String id) {
		try {
			if (!oRep.existsById(Integer.parseInt(id))) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiOcena"), HttpStatus.NOT_FOUND);
			Ocena ocena = oRep.findById(Integer.parseInt(id)).get();
			oRep.delete(ocena); // ako postoji obrisi ocenu
			return new ResponseEntity<>(ocena, HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.POST, value = "/zakljuciPolugodiste/{pg}")
	public ResponseEntity<?> zakljuciPolugodiste(@PathVariable String pg) {
		try {
			//return new ResponseEntity<>(oRep.zakljuci(pg), HttpStatus.OK);
			
			return new ResponseEntity<>(oService.zakljuciOceneNaPolugodistu(pg),HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
		// return new ResponseEntity<>("Ocene su Zakljucene",HttpStatus.OK);

	}
}

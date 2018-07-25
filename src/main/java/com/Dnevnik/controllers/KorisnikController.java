package com.Dnevnik.controllers;

import java.util.Random;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.Dnevnik.dto.KorisnikDto;
import com.Dnevnik.dto.KorisnikDtoPut;
import com.Dnevnik.entities.Admin;
import com.Dnevnik.entities.Korisnik;
import com.Dnevnik.entities.Nastavnik;
import com.Dnevnik.entities.Roditelj;
import com.Dnevnik.entities.Ucenik;
import com.Dnevnik.repositories.AdminRep;
import com.Dnevnik.repositories.KorisnikRep;
import com.Dnevnik.repositories.NastavnikRep;
import com.Dnevnik.repositories.OdelenjeRep;
import com.Dnevnik.repositories.RoditeljRep;
import com.Dnevnik.repositories.UcenikRep;
import com.Dnevnik.security.Pogled;
import com.Dnevnik.services.EmailService;
import com.Dnevnik.services.LoginService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(path = "/dnevnik/korisnik")
@CrossOrigin("http://localhost:4200")
public class KorisnikController {
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private UcenikRep uRep;
	@Autowired
	private RoditeljRep rRep;
	@Autowired
	private NastavnikRep nRep;
	@Autowired
	private AdminRep aRep;
	@Autowired
	private KorisnikRep kRep;
	@Autowired
	private OdelenjeRep oRep;

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> DodajKorisnika(@Valid @RequestBody KorisnikDto kor, BindingResult result) {
		try {
			if (result.hasErrors()) // ako ima greska validacije
				return new ResponseEntity<>(new GreskaValidacije(result), HttpStatus.BAD_REQUEST);// vrati gresku
			String uloga = kor.getUloga();
			kor.setUloga("ROLE_" + uloga);
			switch (uloga) {
			case "U":
				Ucenik noviU = new Ucenik();
				popuni(noviU, kor);
				uRep.save(noviU);
				break;
			case "R":
				Roditelj noviR = new Roditelj();
				popuni(noviR, kor);
				rRep.save(noviR);
				break;
			case "N":
				Nastavnik noviN = new Nastavnik();
				popuni(noviN, kor);
				nRep.save(noviN);
				break;
			case "A":
				Admin noviA = new Admin();
				popuni(noviA, kor);
				aRep.save(noviA);
				break;
			}
			return new ResponseEntity<>(kor, HttpStatus.CREATED);

		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}

	private void popuni(Korisnik novi, KorisnikDto dto) throws Exception {

		novi.setIme(dto.getIme());
		novi.setPrezime(dto.getPrezime());
		novi.setEmail("a@a.m");
		//novi.setEmail(dto.getEmail());
		String sifra = "";
		Random rnd = new Random();
		for (int i = 0; i < 5; i++)
			sifra += (char) (rnd.nextInt(24) + 97);
		System.out.print(sifra);
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		novi.setPassword(bCryptPasswordEncoder.encode("pass")); // sifra je : pass
		// novi.setPassword(bCryptPasswordEncoder.encode(sifra)); // sifra je random 5
		// slova
		novi.setUloga(dto.getUloga());
		String korIme = dto.getIme() + "." + dto.getPrezime();
		Boolean uniq = false;
		Integer temp = 1;
		do { // petlja, ako ime.prezime postoji , dodaj broj na kraj (1,2,3,4...)
			if (kRep.findFirstByLogime(korIme) == null) {
				novi.setLogime(korIme);
				uniq = true;
			} else {
				korIme = dto.getIme() + "." + dto.getPrezime() + temp.toString();
				temp++;
			}
		} while (!uniq);
		EmailObject mail = new EmailObject();
		mail.setTo(novi.getEmail());
		mail.setSubject("Kreiran");
		mail.setText(" Korisnicko ime :" + novi.getLogime() + " <br> Password :" + sifra);
		emailService.sendTemplateMessage(mail);

	}

	/*
	 * ne moze svi korisnici
	 * 
	 * @JsonView(Pogled.Ad.class)
	 * 
	 * @RequestMapping(method = RequestMethod.GET) public ResponseEntity<?>
	 * vratiSveKorisnike() { try { List<Korisnik> lista = (List<Korisnik>)
	 * kRep.findAll(); if (lista == null) // ako nema nijedne vrati gresku return
	 * new ResponseEntity<>(new RESTError("nePostojiKorisnik"),
	 * HttpStatus.NOT_FOUND); return new ResponseEntity<>(lista, HttpStatus.OK);
	 * 
	 * } catch (Exception e) { return new ResponseEntity<>(new
	 * RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST); } }
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> izmeniKorisnika(@PathVariable String id, @RequestBody KorisnikDtoPut korisnikDto) {
		try {
			if (!kRep.existsById(Integer.parseInt(id))) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiKorisnik"), HttpStatus.NOT_FOUND);
			Korisnik korisnik = kRep.findById(Integer.parseInt(id)).get();
			// String uloga = korisnik.getUloga();

			if (korisnikDto.getIme() != null)
				korisnik.setIme(korisnikDto.getIme());
			if (korisnikDto.getPrezime() != null)
				korisnik.setPrezime(korisnikDto.getPrezime());
			if (korisnikDto.getEmail() != null)
				korisnik.setEmail(korisnikDto.getEmail());
			return new ResponseEntity<>(kRep.save(korisnik), HttpStatus.OK);
			/*
			 * switch (uloga) { case "ROLE_U": Ucenik noviU = (Ucenik) korisnik; return new
			 * ResponseEntity<>(uRep.save(noviU), HttpStatus.OK); case "ROLE_R": Roditelj
			 * noviR = (Roditelj) korisnik; return new ResponseEntity<>(rRep.save(noviR),
			 * HttpStatus.OK); case "ROLE_N": Nastavnik noviN = (Nastavnik) korisnik; return
			 * new ResponseEntity<>(nRep.save(noviN), HttpStatus.OK); case "ROLE_A": Admin
			 * noviA = (Admin) korisnik; return new ResponseEntity<>(aRep.save(noviA),
			 * HttpStatus.OK); } return null;
			 */
		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}

	}

	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> vratiKorisnikaId(@PathVariable String id) {
		try {
			if (!kRep.existsById(Integer.parseInt(id))) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiKorisnik"), HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(kRep.findById(Integer.parseInt(id)), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> obrisiKorisnikaId(@PathVariable String id) {
		try {
			Integer idk = Integer.parseInt(id);
			if (!kRep.existsById(idk)) // ako ne postoji vrati gresku
				return new ResponseEntity<>(new RESTError("nePostojiKorisnik"), HttpStatus.NOT_FOUND);
			Korisnik korisnik = kRep.findById(idk).get();
			String uloga = korisnik.getUloga();
			uloga = uloga.substring(5);
			switch (uloga) {
			case "U":
				Ucenik noviU = uRep.findById(idk).get();
				if (noviU.getCasovi().size() != 0 || noviU.getOcena().size() != 0) // ako se koristi u bazi ne moze se brisati
					return new ResponseEntity<>(new RESTError("zabranjenoBrisanje"), HttpStatus.BAD_REQUEST);
				kRep.delete(korisnik);
				break;
			case "R":
				Roditelj noviR = rRep.findById(idk).get();
				if (noviR.getRoditelj().size() != 0)
					return new ResponseEntity<>(new RESTError("zabranjenoBrisanje"), HttpStatus.BAD_REQUEST);
				kRep.delete(korisnik);
				break;
			case "N":
				Nastavnik noviN = nRep.findById(idk).get();
				if (noviN.getLicenca().size() != 0 && oRep.existsByRazredni(noviN))
					return new ResponseEntity<>(new RESTError("zabranjenoBrisanje"), HttpStatus.BAD_REQUEST);
				kRep.delete(korisnik);
				break;
			case "A":
				kRep.delete(korisnik);
				break;
			}
			return new ResponseEntity<>(korisnik, HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
	
	@JsonView(Pogled.Ad.class)
	@RequestMapping(method = RequestMethod.GET, value = "/token")
	public ResponseEntity<?> vratiKorisnikaToken() {
		try {			
			return new ResponseEntity<>(loginService.getKorisnikFromToken(), HttpStatus.OK);

		} catch (NumberFormatException e) {
			return new ResponseEntity<>(new RESTError("brojNijeInteger"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new RESTError("koZnaStaJe"), HttpStatus.BAD_REQUEST);
		}
	}
}

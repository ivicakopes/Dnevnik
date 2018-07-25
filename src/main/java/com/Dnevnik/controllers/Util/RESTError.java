package com.Dnevnik.controllers.Util;

import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

public class RESTError {

	@JsonView(Pogled.Uc.class)
	private int code;
	@JsonView(Pogled.Uc.class)
	private String message;
// prosledjena greska kao string , a vraca kod greske i poruku
	public RESTError(String greska,Integer broj) {
		this.code = 100;
		this.message = greska;		
	}
	
	public RESTError(String greska) { // konstruktor 
		if (greska.equals("nePostoji")) {
			this.code = 10;
			this.message = "Podatak ne postoji u bazi !";
				
		} else if (greska.equals("nePostojiKorisnik")) {
			this.code = 11;
			this.message = "Takav korisnik  ne postoji u bazi !";
		
		} else if (greska.equals("nePostojiUcenik")) {
			this.code = 12;
			this.message = "Takav ucenik  ne postoji u bazi !";
			
		} else if (greska.equals("nePostojiRoditelj")) {
			this.code = 13;
			this.message = "Takav roditelj  ne postoji u bazi !";
		
		} else if (greska.equals("nePostojiNastavnik")) {
			this.code = 14;
			this.message = "Takav nastavnik ne postoji u bazi !";
		
		} else if (greska.equals("nePostojiAdmin")) {
			this.code = 15;
			this.message = "Takav admin ne postoji u bazi !";
				
		} else if (greska.equals("nePostojiRazred")) {
			this.code = 16;
			this.message = "Takav razred ne postoji u bazi !";
		
		} else if (greska.equals("nePostojiPredmet")) {
			this.code = 17;
			this.message = "Takav predmet ne postoji u bazi !";
		
		} else if (greska.equals("nePostojiPlan")) {
			this.code = 18;
			this.message = "Takav plan ne postoji u bazi !";
		
		} else if (greska.equals("nePostojiLicenca")) {
			this.code = 19;
			this.message = "Takva licenca ne postoji u bazi !";
		
		} else if (greska.equals("nePostojiOdelenje")) {
			this.code = 20;
			this.message = "Takvo odelenje ne postoji u bazi !";
		
		} else if (greska.equals("nePostojiRaspored")) {
			this.code = 21;
			this.message = "Takav raspored ne postoji u bazi !";
		
		} else if (greska.equals("nePostojiCas")) {
			this.code = 22;
			this.message = "Takav cas ne postoji u bazi !";
		
		} else if (greska.equals("nePostojiVrsta")) {
			this.code = 23;
			this.message = "Takva vrsta ocene ne postoji u bazi !";
		
		} else if (greska.equals("nePostojiOcena")) {
			this.code = 24;
			this.message = "Ta ocena ne postoji u bazi !";
		
		} else if (greska.equals("nePostojiOdsutan")) {
			this.code = 25;
			this.message = "Takav podatak za odsutnog ne postoji u bazi !";
		
				
		} else if (greska.equals("nePostoje")) {
			this.code = 29;
			this.message = "Takvi podaci ne postoje u bazi ! ";
			
			
		}else if (greska.equals("vecPostoji")) {
			this.code = 30;
			this.message = "Uneti podatak već postoji u bazi !";
		
		} else if (greska.equals("vecPostojiKorisnik")) {
			this.code = 31;
			this.message = "Takav korisnik vec postoji u bazi !";
		
		} else if (greska.equals("vecPostojiUcenik")) {
			this.code = 32;
			this.message = "Takav ucenik  vec postoji u bazi !";
			
		} else if (greska.equals("vecPostojiRoditelj")) {
			this.code = 33;
			this.message = "Takav roditelj  vec postoji u bazi !";
		
		} else if (greska.equals("vecPostojiNastavnik")) {
			this.code = 34;
			this.message = "Takav nastavnik vec postoji u bazi !";
		
		} else if (greska.equals("vecPostojiAdmin")) {
			this.code = 35;
			this.message = "Takav admin vec postoji u bazi !";
				
		} else if (greska.equals("vecPostojiRazred")) {
			this.code = 36;
			this.message = "Takav razred vec postoji u bazi !";
		
		} else if (greska.equals("vecPostojiPredmet")) {
			this.code = 37;
			this.message = "Takav predmet vec postoji u bazi !";
		
		} else if (greska.equals("vecPostojiPlan")) {
			this.code = 38;
			this.message = "Takav plan vec postoji u bazi !";
		
		} else if (greska.equals("vecPostojiLicenca")) {
			this.code = 39;
			this.message = "Takva licenca vec postoji u bazi !";
		
		} else if (greska.equals("vecPostojiOdelenje")) {
			this.code = 40;
			this.message = "Takvo odelenje vec postoji u bazi !";
		
		} else if (greska.equals("vecPostojiRaspored")) {
			this.code = 41;
			this.message = "Takav raspored vec postoji u bazi !";
		
		} else if (greska.equals("vecPostojiCas")) {
			this.code = 42;
			this.message = "Takav cas vec postoji u bazi !";
		
		} else if (greska.equals("vecPostojiVrsta")) {
			this.code = 43;
			this.message = "Takva vrsta ocene vec postoji u bazi !";
		
		} else if (greska.equals("vecPostojiOcena")) {
			this.code = 44;
			this.message = "Ta ocena vec postoji u bazi !";
		
		} else if (greska.equals("vecPostojiOdsutan")) {
			this.code = 45;
			this.message = "Takav podatak za odsutnog vec postoji u bazi !";
		
				
		} else if (greska.equals("vecPostoje")) {
			this.code = 49;
			this.message = "Takvi podaci vec postoje u bazi ! ";
			
		
		}  else if (greska.equals("exception")) {
			this.code = 60;
			this.message = "Exception occurred: ";
				
		}else if (greska.equals("pass nije isti")) {
			this.code = 61;
			this.message = "Password i ponovljeni Password se ne podudaraju !";
		
		} else if (greska.equals("nePostojiTekuca")) {
			this.code = 62;
			this.message = "Nije uneta tekuca godina !";
		
			
		} else if (greska.equals("IDnijeInteger")) {
			this.code = 70;
			this.message = "Morate uneti ID broj !";
		
		}   else if (greska.equals("brojNijeInteger")) {
			this.code = 71;
			this.message = "Uneli ste znak ili slovo , morate uneti broj !";
		
		} else if (greska.equals("razlicitaDuzina")) {
			this.code = 72;
			this.message = "Morate uneti tacno 8 cifara za fond !";
		
		}else if (greska.equals("neispravan user")) {
			this.code = 73;
			this.message = "Prosledite zahtevane podatke !";
			
		}else if (greska.equals("IdIliRazredNijeInteger")) {
			this.code = 74;
			this.message = "Id i fond moraju biti brojevi !";
			
		}else if (greska.equals("greskaOdelenjeRazred")) {
			this.code = 75;
			this.message = "Ucenik moze biti samo u odelenju koje pripada odredjenom razredu !";
			
		}else if (greska.equals("greskaUcenikOdelenje")) {
			this.code = 76;
			this.message = "Ucenik ne pripada tom rasporedu (odelenje ili licenca su pogresni) !";
			
		}else if (greska.equals("neispravanDatum")) {
			this.code = 76;
			this.message = "Datum nije ispravno napisan ili nije dobar !";
			
		}else if (greska.equals("zabranjenoBrisanje")) {
			this.code = 77;
			this.message = "Zabranjeno Brisanje jer se podatak koristi u bazi !";
			
			
			
		} else {
			this.code = 0;
			this.message = "Nije definisana greska !";
		}
	}

	public RESTError(int code, String message) { // konstruktor 
		super();
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
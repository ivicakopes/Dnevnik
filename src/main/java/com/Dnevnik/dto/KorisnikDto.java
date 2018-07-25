package com.Dnevnik.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

public class KorisnikDto {
	
	@JsonView(Pogled.Uc.class)
	@NotNull(message = "Morate uneti Ime !")
	@Pattern(regexp = "^([A-Z][a-z]+)+(([ ][A-Z][a-z]+)*)$",	message="Ime mora da sadrzi samo slova ili space vezu za dva imena, prvo slovo veliko")
	private String ime;
	
	@JsonView(Pogled.Uc.class)
	@Pattern(regexp = "^([A-Z][a-z]+)+((([ ]|[-])[A-Z][a-z]+)*)$",	message="Prezime mora da sadrzi samo slova ili space ili - za dva prezimena, prvo slovo veliko")
	@NotNull(message = "Morate uneti Prezime !") 	
	private String prezime;
	
	@JsonView(Pogled.Uc.class)
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",	message = "E-mail nije ispravan format !")
	@NotNull(message = "Morate uneti E-mail !") 	
	private String email;
	
	@JsonView(Pogled.Uc.class)
	@NotNull(message = "Morate uneti ulogu !") 
	@Pattern(regexp = "^[URNA]$", message="Uloga moze biti U R N A")
	private String uloga;

	public KorisnikDto() {
		super();
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUloga() {
		return uloga;
	}

	public void setUloga(String uloga) {
		this.uloga = uloga;
	}


	
	
	
}

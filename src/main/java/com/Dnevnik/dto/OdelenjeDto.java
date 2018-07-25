package com.Dnevnik.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

public class OdelenjeDto {

	@JsonView(Pogled.Uc.class)
	@NotNull(message = "Morate uneti razred !")
	@Pattern(regexp = "^[1-8]$",message = "Razred je od 1 do 8 !")
	private String razred;

	@JsonView(Pogled.Uc.class)
	@NotNull(message = "Morate uneti naziv odelenja !")
	@Pattern(regexp = "^[1-9]$",message = "Odelenje je od 1 do 9 !")
	private String naziv;

	public OdelenjeDto() {
		super();
	}

	
	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}


	public String getRazred() {
		return razred;
	}


	public void setRazred(String razred) {
		this.razred = razred;
	}
	
	
}

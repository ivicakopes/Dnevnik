package com.Dnevnik.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

public class PredmetDto {

	@JsonView(Pogled.Uc.class)
	@NotNull(message = "Morate uneti naziv predmeta !")
	@Pattern(regexp = "^([a-z]+)+(([ ][a-z]+)*)$",	message="Naziv mora da sadrzi samo mala slova ili razmak za vezu dve reči")
	private String naziv;

	public PredmetDto() {
		super();
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}	
}

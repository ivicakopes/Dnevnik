package com.Dnevnik.dto;

import javax.validation.constraints.Pattern;

import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

public class UcenikDto {

	@JsonView(Pogled.Uc.class)
	@Pattern(regexp = "^[1-8]$",message = "Razred je od 1 do 8 !")
	private String razred;

	@JsonView(Pogled.Uc.class)
	@Pattern(regexp = "^[1-9]*$",message = "Odelenje je od 1 do 9 !")
		private String odelenje;

	@JsonView(Pogled.Uc.class)
	@Pattern(regexp = "^[0-9]*$", message = "Morate uneti id roditelja , samo cifre su dozvoljene !")
		private String roditelj;

	public UcenikDto() {
		super();
	}

	public String getRazred() {
		return razred;
	}

	public void setRazred(String razred) {
		this.razred = razred;
	}

	public String getOdelenje() {
		return odelenje;
	}

	public void setOdelenje(String odelenje) {
		this.odelenje = odelenje;
	}

	public String getRoditelj() {
		return roditelj;
	}

	public void setRoditelj(String roditelj) {
		this.roditelj = roditelj;
	}
	
	
	
}

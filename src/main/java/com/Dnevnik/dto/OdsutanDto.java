package com.Dnevnik.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

public class OdsutanDto {

	@JsonView(Pogled.Nas.class)
	@NotNull(message = "Morate uneti id ucenika !")
	@Pattern(regexp = "^[0-9]*$",message = "Morate uneti id ucenika , samo cifre su dozvoljene !")
	private String ucenik;

	@JsonView(Pogled.Nas.class)
	@NotNull(message = "Morate uneti id casa !")
	@Pattern(regexp = "^[0-9]*$",message = "Morate uneti id casa , samo cifre su dozvoljene !")
	private String cas;

	public OdsutanDto() {
		super();
	}

	public String getUcenik() {
		return ucenik;
	}

	public void setUcenik(String ucenik) {
		this.ucenik = ucenik;
	}

	public String getCas() {
		return cas;
	}

	public void setCas(String cas) {
		this.cas = cas;
	}

	
	
}

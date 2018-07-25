package com.Dnevnik.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

public class RazredniDto {

	@JsonView(Pogled.Uc.class)
	@NotNull(message = "Morate uneti ID odelenja !")
	@Pattern(regexp = "^[0-9]*$",	message = "Unos za odelenje mora biti Id broj odelenja")
	private String odelenjeId;
	
	@JsonView(Pogled.Uc.class)
	@NotNull(message = "Morate uneti ID nastavnika !")
	@Pattern(regexp = "^[0-9]*$",	message = "Unos za nastavnika mora biti njegov Id broj")
	private String nastavnikId;

	public RazredniDto() {
		super();
	}

	public String getOdelenjeId() {
		return odelenjeId;
	}

	public void setOdelenjeId(String odelenjeId) {
		this.odelenjeId = odelenjeId;
	}

	public String getNastavnikId() {
		return nastavnikId;
	}

	public void setNastavnikId(String nastavnikId) {
		this.nastavnikId = nastavnikId;
	}
	
	
}

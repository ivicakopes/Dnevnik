package com.Dnevnik.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

public class LicencaDto {

	@JsonView(Pogled.Uc.class)
	@NotNull(message = "Morate uneti ID nastavnika !")
	@Pattern(regexp = "^[0-9]*$",	message="Unos za nastavnika mora biti njegov Id broj")
	private String nastavnik;

	@JsonView(Pogled.Uc.class)
	@NotNull(message = "Morate uneti koje predmete nastavnik moze da predaje (plan ID), razdvojene zarezom !")
	@Pattern(regexp = "^(([0-9]*)+([,][0-9]*)*)$",	message="Unos za planId mora biti njegov Id broj , razdvojeni zarezom")
	private String planId;

	public LicencaDto() {
		super();
	}

	public String getNastavnik() {
		return nastavnik;
	}

	public void setNastavnik(String nastavnik) {
		this.nastavnik = nastavnik;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	
	
	
}

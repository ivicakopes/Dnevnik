package com.Dnevnik.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class RasporedDto {

	@NotNull(message = "Morate uneti id licence !")
	@Pattern(regexp = "^[0-9]*$",message = "Morate uneti id licence , samo cifre su dozvoljene !")
	private String licenca;
	
	@NotNull(message = "Morate uneti id odelenja !")
	@Pattern(regexp = "^[0-9]*$",message = "Morate uneti id odelenja , samo cifre su dozvoljene !")
	private String odelenje;

	public RasporedDto() {
		super();
	}

	public String getLicenca() {
		return licenca;
	}

	public void setLicenca(String licenca) {
		this.licenca = licenca;
	}

	public String getOdelenje() {
		return odelenje;
	}

	public void setOdelenje(String odelenje) {
		this.odelenje = odelenje;
	}

	
}

package com.Dnevnik.dto;

import javax.validation.constraints.Pattern;

import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

public class OcenaDtoPut {

	@JsonView(Pogled.Nas.class)
	@Pattern(regexp = "^[0-9]*$", message = "Morate uneti id ucenika , samo cifre su dozvoljene !")
	private String ucenik;

	@JsonView(Pogled.Nas.class)
	@Pattern(regexp = "^[0-9]*$", message = "Morate uneti id rasporeda , samo cifre su dozvoljene !")
	private String raspored;

	@JsonView(Pogled.Nas.class)
	@Pattern(regexp = "^[1-5]$", message = "Ocene su od 1 do 5 !")
	private String ocena;

	@JsonView(Pogled.Nas.class)
	@Pattern(regexp = "^[UKPZR]$", message = "Moguca vrsta je:  U -usmeni, K - kontrolni, P -pismeni, Z -zalaganje, R -rad !")
	private String vrsta;

	@JsonView(Pogled.Nas.class)
	@Pattern(regexp = "^[12]$", message = "Polugodiste moze biti 1 ili 2 !")
	private String polugodiste;

	@JsonView(Pogled.Nas.class)
	@Pattern(regexp = "^[0-3]{1}[0-9]{1}[.]{1}[0-1]{1}[0-9]{1}[.]{1}[2]{1}[0]{1}[1-3]{1}[0-9]{1}$", message = "Format datuma ne odgovara !")
	private String datum;

	@JsonView(Pogled.Nas.class)
	private String opis;

	public OcenaDtoPut() {
		super();
	}

	public String getUcenik() {
		return ucenik;
	}

	public void setUcenik(String ucenik) {
		this.ucenik = ucenik;
	}

	public String getRaspored() {
		return raspored;
	}

	public void setRaspored(String raspored) {
		this.raspored = raspored;
	}

	public String getOcena() {
		return ocena;
	}

	public void setOcena(String ocena) {
		this.ocena = ocena;
	}

	public String getVrsta() {
		return vrsta;
	}

	public void setVrsta(String vrsta) {
		this.vrsta = vrsta;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public String getPolugodiste() {
		return polugodiste;
	}

	public void setPolugodiste(String polugodiste) {
		this.polugodiste = polugodiste;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

}

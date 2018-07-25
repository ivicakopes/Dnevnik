package com.Dnevnik.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Nastavnik extends Korisnik {

	
	@JsonIgnore
	@OneToMany(mappedBy = "nastavnik", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Licenca> licenca = new ArrayList<>();

	public Nastavnik() {
		super();
	}
	
	public List<Licenca> getLicenca() {
		return licenca;
	}

	public void setLicenca(List<Licenca> licenca) {
		this.licenca = licenca;
	}

}

package com.Dnevnik.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Roditelj extends Korisnik {

	@JsonBackReference
	@OneToMany(mappedBy = "roditelj", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Ucenik> roditelj = new ArrayList<>();

	public Roditelj() {
		super();
	}

	public List<Ucenik> getRoditelj() {
		return roditelj;
	}

	public void setRoditelj(List<Ucenik> roditelj) {
		this.roditelj = roditelj;
	}

}

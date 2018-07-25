package com.Dnevnik.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Ucenik extends Korisnik {

	@JsonView(Pogled.Uc.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Razred razred;

	@JsonView(Pogled.Uc.class)
	@JsonManagedReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Odelenje odelenje;

	@JsonView(Pogled.Uc.class)
	@JsonManagedReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Roditelj roditelj;

	@JsonIgnore
	@JsonBackReference 
	@OneToMany(mappedBy = "ucenik", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Odsutan> casovi = new ArrayList<>();

	@JsonIgnore
	@JsonBackReference
	@OneToMany(mappedBy = "ucenik", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Ocena> ocena = new ArrayList<>();

	
	public Ucenik() {
		super();
	}

	public List<Odsutan> getCasovi() {
		return casovi;
	}

	public void setCasovi(List<Odsutan> casovi) {
		this.casovi = casovi;
	}

	public List<Ocena> getOcena() {
		return ocena;
	}

	public void setOcena(List<Ocena> ocena) {
		this.ocena = ocena;
	}

	public Roditelj getRoditelj() {
		return roditelj;
	}

	public void setRoditelj(Roditelj roditelj) {
		this.roditelj = roditelj;
	}

	public Odelenje getOdelenje() {
		return odelenje;
	}

	public void setOdelenje(Odelenje odelenje) {
		this.odelenje = odelenje;
	}

	public Razred getRazred() {
		return razred;
	}

	public void setRazred(Razred razred) {
		this.razred = razred;
	}

}

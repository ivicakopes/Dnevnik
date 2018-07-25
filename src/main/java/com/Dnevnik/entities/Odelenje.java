package com.Dnevnik.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Odelenje {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@JsonView(Pogled.Uc.class)
	private Integer id;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonView(Pogled.Uc.class)
	@JoinColumn(name = "razred")
	private Razred razred;
	
	@JsonView(Pogled.Uc.class)
	private String naziv;

	@JsonIgnore	
	@OneToMany(mappedBy = "odelenje", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Ucenik> ucenici;

	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "razredni")
	@JsonView(Pogled.Uc.class)
	private Nastavnik razredni;

	@JsonIgnore
	@JsonBackReference
	@OneToMany(mappedBy = "odelenje", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Raspored> raspored;
	
	@Version
	@JsonView(Pogled.Ad.class)
	private Integer version;

	public Odelenje() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public Nastavnik getRazredni() {
		return razredni;
	}

	public void setRazredni(Nastavnik razredni) {
		this.razredni = razredni;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public List<Raspored> getRaspored() {
		return raspored;
	}

	public void setRaspored(List<Raspored> raspored) {
		this.raspored = raspored;
	}

	public List<Ucenik> getUcenici() {
		return ucenici;
	}

	public void setUcenici(List<Ucenik> ucenici) {
		this.ucenici = ucenici;
	}

	public Razred getRazred() {
		return razred;
	}

	public void setRazred(Razred razred) {
		this.razred = razred;
	}

}

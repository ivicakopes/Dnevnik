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
import javax.persistence.Version;

import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Raspored {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@JsonView(Pogled.Uc.class)
	private Integer id;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name = "licenca")
	@JsonView(Pogled.Uc.class)
	private Licenca licenca;

	@JsonBackReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "odelenje")
	@JsonView(Pogled.Uc.class)
	private Odelenje odelenje;

	@JsonBackReference
	@JsonView(Pogled.Uc.class)
	@OneToMany(mappedBy = "raspored", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Cas> casovi;

	@JsonBackReference
	@JsonView(Pogled.Uc.class)
	@OneToMany(mappedBy = "raspored", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Ocena> ocene;
	
	@Version
	@JsonView(Pogled.Ad.class)
	private Integer version;

	public Raspored() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Licenca getLicenca() {
		return licenca;
	}

	public void setLicenca(Licenca licenca) {
		this.licenca = licenca;
	}

	public Odelenje getOdelenje() {
		return odelenje;
	}

	public void setOdelenje(Odelenje odelenje) {
		this.odelenje = odelenje;
	}

	public List<Cas> getCasovi() {
		return casovi;
	}

	public void setCasovi(List<Cas> casovi) {
		this.casovi = casovi;
	}

	public List<Ocena> getOcene() {
		return ocene;
	}

	public void setOcene(List<Ocena> ocene) {
		this.ocene = ocene;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}

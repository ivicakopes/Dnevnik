package com.Dnevnik.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Razred {
	@Id
	@JsonView(Pogled.Uc.class)
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;

	@JsonView(Pogled.Uc.class)
	private Integer razred;

	@Version
	@JsonView(Pogled.Ad.class)
	private Integer version;

	

	@JsonBackReference
	@OneToMany(mappedBy = "razred", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Odelenje> odelenja;

	@JsonBackReference
	@OneToMany(mappedBy = "razred", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Ucenik> ucenici;
	
	@JsonBackReference
	@OneToMany(mappedBy = "razred", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Plan> plan;
	
	public Razred() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRazred() {
		return razred;
	}

	public void setRazred(Integer razred) {
		this.razred = razred;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	

	public List<Odelenje> getOdelenja() {
		return odelenja;
	}

	public void setOdelenja(List<Odelenje> odelenja) {
		this.odelenja = odelenja;
	}

	public List<Ucenik> getUcenici() {
		return ucenici;
	}

	public void setUcenici(List<Ucenik> ucenici) {
		this.ucenici = ucenici;
	}

}

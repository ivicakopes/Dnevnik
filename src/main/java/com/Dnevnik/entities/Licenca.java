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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Licenca {
	@Id
	@JsonView(Pogled.Uc.class)
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonView(Pogled.Uc.class)
	@JoinColumn(name = "plan")
	private Plan plan;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonView(Pogled.Uc.class)
	@JoinColumn(name = "nastavnik")
	private Nastavnik nastavnik;

	@JsonBackReference
	@OneToMany(mappedBy = "licenca", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Raspored> raspored;
	
	@Version
	@JsonView(Pogled.Ad.class)
	private Integer version;

	public Licenca() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Nastavnik getNastavnik() {
		return nastavnik;
	}

	public void setNastavnik(Nastavnik nastavnik) {
		this.nastavnik = nastavnik;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public List<Raspored> getRaspored() {
		return raspored;
	}

	public void setRaspored(List<Raspored> raspored) {
		this.raspored = raspored;
	}
}

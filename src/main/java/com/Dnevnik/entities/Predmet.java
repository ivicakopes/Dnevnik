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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Predmet {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@JsonView(Pogled.Uc.class)
	private Integer id;

	@JsonView(Pogled.Uc.class)
	private String naziv;

	@Version
	@JsonIgnore
	@JsonView(Pogled.Ad.class)
	private Integer version;	
	
	@JsonBackReference
	@OneToMany(mappedBy = "predmet", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Plan> plan;

	public Predmet() {
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public List<Plan> getPlan() {
		return plan;
	}

	public void setPlan(List<Plan> plan) {
		this.plan = plan;
	}

	

}

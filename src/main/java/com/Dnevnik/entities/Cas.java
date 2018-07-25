package com.Dnevnik.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Cas {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@JsonView(Pogled.Uc.class)
	private Integer id;	
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonManagedReference
	@JsonView(Pogled.Uc.class)
	@JoinColumn(name = "raspored")
	private Raspored raspored;	

	@JsonView(Pogled.Uc.class)
	private LocalDateTime pocetak;

	@JsonView(Pogled.Uc.class)
	private LocalDateTime zavrsetak;

	@JsonView(Pogled.Uc.class)
	private String tema;

	@JsonView(Pogled.Uc.class)
	private String beleske;
	
	@JsonIgnore
	@OneToMany(mappedBy = "cas", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Odsutan> ucenici = new ArrayList<>();
		
	@Version
	@JsonView(Pogled.Ad.class)
	private Integer version;

	public Cas() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public String getBeleske() {
		return beleske;
	}

	public void setBeleske(String beleske) {
		this.beleske = beleske;
	}

	public List<Odsutan> getUcenici() {
		return ucenici;
	}

	public void setUcenici(List<Odsutan> ucenici) {
		this.ucenici = ucenici;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public LocalDateTime getPocetak() {
		return pocetak;
	}

	public void setPocetak(LocalDateTime pocetak) {
		this.pocetak = pocetak;
	}

	public LocalDateTime getZavrsetak() {
		return zavrsetak;
	}

	public void setZavrsetak(LocalDateTime zavrsetak) {
		this.zavrsetak = zavrsetak;
	}

	public Raspored getRaspored() {
		return raspored;
	}

	public void setRaspored(Raspored raspored) {
		this.raspored = raspored;
	}

}

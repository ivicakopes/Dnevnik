package com.Dnevnik.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Version;

import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Korisnik {

	@Id
	@JsonView(Pogled.Uc.class)
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;
	
	@JsonView(Pogled.Uc.class)
	private String ime;
	
	@JsonView(Pogled.Uc.class)
	private String prezime;
	
	@JsonView(Pogled.Uc.class)
	private String email;
	
	@JsonView(Pogled.Uc.class)
	private String logime;
	
	@JsonIgnore
	private String password;
	
	@JsonView(Pogled.Uc.class)
	private String uloga;

	@Version
	@JsonView(Pogled.Ad.class)
	private Integer version;

	public Korisnik() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogime() {
		return logime;
	}

	public void setLogime(String logime) {
		this.logime = logime;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getUloga() {
		return uloga;
	}

	public void setUloga(String uloga) {
		this.uloga = uloga;
	}

}

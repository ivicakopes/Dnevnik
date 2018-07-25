package com.Dnevnik.entities;

import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Pattern;

import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Ocena {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@JsonView(Pogled.Uc.class)
	private Integer id;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonView(Pogled.Uc.class)
	@JsonManagedReference
	@JoinColumn(name = "ucenik")
	private Ucenik ucenik;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name = "raspored")
	@JsonView(Pogled.Uc.class)
	private Raspored raspored;
	
	@JsonView(Pogled.Uc.class)
	private Integer ocena;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "vrsta")
	@JsonView(Pogled.Uc.class)
	private Vrsta vrsta;
	
	@JsonView(Pogled.Nas.class)	
	@Pattern(regexp = "^[12]$",message = "Polugodiste moze biti 1 ili 2 !")
	private String polugodiste;
	

	@JsonView(Pogled.Uc.class)
	private String opis;

	@JsonView(Pogled.Uc.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING,
			pattern = "dd.MM.yyyy")
	private LocalDate datum;

	@JsonView(Pogled.Ad.class)
	@Version
	private Integer version;

	public Ocena() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Ucenik getUcenik() {
		return ucenik;
	}

	public void setUcenik(Ucenik ucenik) {
		this.ucenik = ucenik;
	}

	public Raspored getRaspored() {
		return raspored;
	}

	public void setRaspored(Raspored raspored) {
		this.raspored = raspored;
	}

	public Integer getOcena() {
		return ocena;
	}

	public void setOcena(Integer ocena) {
		this.ocena = ocena;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public LocalDate getDatum() {
		return datum;
	}

	public void setDatum(LocalDate localDate) {
		this.datum = localDate;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getPolugodiste() {
		return polugodiste;
	}

	public void setPolugodiste(String polugodiste) {
		this.polugodiste = polugodiste;
	}

	public Vrsta getVrsta() {
		return vrsta;
	}

	public void setVrsta(Vrsta vrsta) {
		this.vrsta = vrsta;
	}

}

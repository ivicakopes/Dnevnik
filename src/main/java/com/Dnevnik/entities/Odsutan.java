package com.Dnevnik.entities;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import com.Dnevnik.enumeration.Status_odsutan;
import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Odsutan {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)	
	@JsonView(Pogled.Uc.class)
	private Integer id;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name = "cas")
	@JsonView(Pogled.Uc.class)
	private Cas cas;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name = "ucenik")
	@JsonView(Pogled.Uc.class)
	private Ucenik ucenik;
	
	@JsonView(Pogled.Uc.class)
	private Status_odsutan status;
	
	@Version
	@JsonView(Pogled.Ad.class)
	private Integer version;

	public Odsutan() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Cas getCas() {
		return cas;
	}

	public void setCas(Cas cas) {
		this.cas = cas;
	}

	public Ucenik getUcenik() {
		return ucenik;
	}

	public void setUcenik(Ucenik ucenik) {
		this.ucenik = ucenik;
	}

	public Status_odsutan getStatus() {
		return status;
	}

	public void setStatus(Status_odsutan status) {
		this.status = status;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}

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
public class Vrsta {
	@Id
	@JsonView(Pogled.Uc.class)
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;
	
	@JsonView(Pogled.Uc.class)
	private String naziv;

	@Version
	@JsonView(Pogled.Ad.class)
	private Integer version;
	
	@JsonBackReference
	@OneToMany(mappedBy = "vrsta", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Vrsta> vrsta;
}

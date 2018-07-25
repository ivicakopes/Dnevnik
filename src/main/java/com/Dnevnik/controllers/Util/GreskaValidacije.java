package com.Dnevnik.controllers.Util;

import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

public class GreskaValidacije {

	@JsonView(Pogled.Uc.class)
	private String poruka;

	// na osnovu resultata generise gresku
	public GreskaValidacije(BindingResult result) {
		this.poruka = result.getAllErrors().stream().map(ObjectError::getDefaultMessage)
				.collect(Collectors.joining(" "));
	}

	public String getMessage() {
		return poruka;
	}

	public void setMessage(String message) {
		this.poruka = message;
	}
}

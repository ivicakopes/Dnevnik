package com.Dnevnik.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.Dnevnik.security.Pogled;
import com.fasterxml.jackson.annotation.JsonView;

public class Predmet_razredDto {

	@JsonView(Pogled.Nas.class)
	@NotNull(message = "Morate uneti id predmeta !")
	@Pattern(regexp = "^[0-9]*$", message = "Morate uneti id predmeta , samo cifre su dozvoljene !")
	private String id;

/*	@JsonView(Pogled.Uc.class)
	@NotNull(message = "Morate uneti za koji razred je predmet !")
	@Size(max = 8)
	@Pattern(regexp = "^(?![1-8]*([1-8])[1-8]*\1)[1-8]+$", message = "Razred mora sadrzati samo cifre 1-8")
	private String razred;
*/
	@JsonView(Pogled.Uc.class)
	@NotNull(message = "Morate uneti fond casova za predmet ,za sve razrede !")
	@Pattern(regexp = "^[0-9]{8}$", message = "Fond mora sadrzati samo cifre 0-9")
	private String fond;

	public Predmet_razredDto() {
		super();
	}

	/*	public String getRazred() {
		return razred;
	}

	public void setRazred(String razred) {
		this.razred = razred;
	}*/

	public String getFond() {
		return fond;
	}

	public void setFond(String fond) {
		this.fond = fond;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}

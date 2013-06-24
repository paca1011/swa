package de.shop.artikelverwaltung.service;

import java.util.Collection;

import javax.validation.ConstraintViolation;

import de.shop.artikelverwaltung.domain.Artikel;

public class InvalidBezeichnungException extends AbstractArtikelValidationException {
	private static final long serialVersionUID = -8973151010781329074L;
	
	private final String bezeichnung;
	
	public InvalidBezeichnungException(String bezeichnung, Collection<ConstraintViolation<Artikel>> violations) {
		super(violations);
		this.bezeichnung = bezeichnung;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}
}


package de.shop.artikelverwaltung.service;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.util.IdGroup;
import de.shop.util.Log;
import de.shop.util.Mock;
import de.shop.util.ValidatorProvider;

@Log
public class ArtikelService implements Serializable {
	private static final long serialVersionUID = -5105686816948437276L;
	
	@Inject
	private ValidatorProvider validatorProvider;

	private void validateArtikel(Artikel artikel, Locale locale, Class<?>... groups) {
		// Werden alle Constraints beim Einfuegen gewahrt?
		final Validator validator = validatorProvider.getValidator(locale);
		
		final Set<ConstraintViolation<Artikel>> violations = validator.validate(artikel, groups);
		if (!violations.isEmpty()) {
			throw new InvalidArtikelException(artikel, violations);
		}
	}
	
	public List<Artikel> findArtikelByBezeichnung(String bezeichnung, Locale locale) {
		validateBezeichnung(bezeichnung, locale);
		
		// TODO Datenbanzugriffsschicht statt Mock
		final List<Artikel> vieleartikel = Mock.findArtikelByBezeichnung(bezeichnung);
		return vieleartikel;
	}
	
	private void validateBezeichnung(String bezeichnung, Locale locale) {
		final Validator validator = validatorProvider.getValidator(locale);
		final Set<ConstraintViolation<Artikel>> violations = validator.validateValue(Artikel.class,
				                                                                           "bezeichnung",
				                                                                           bezeichnung,
				                                                                           Default.class);
		if (!violations.isEmpty())
			throw new InvalidBezeichnungException(bezeichnung, violations);
	}
	
	public Artikel findArtikelById(Long id) {
		// TODO id pruefen
		// TODO Datenbanzugriffsschicht statt Mock
		return Mock.findArtikelById(id);
	}
	
	public Artikel createArtikel(Artikel artikel, Locale locale) {
		if (artikel == null) {
			return artikel;
		}
		validateArtikel(artikel, locale, Default.class);
		
		artikel = Mock.createArtikel(artikel);

		return artikel;
	}
	
	public Artikel updateArtikel(Artikel artikel, Locale locale) {
		if (artikel == null) {
			return null;
		}

		// Werden alle Constraints beim Modifizieren gewahrt?
		validateArtikel(artikel, locale, Default.class, IdGroup.class);

		
		// TODO Datenbanzugriffsschicht statt Mock
		Mock.updateArtikel(artikel);
		
		return artikel;
	}
}

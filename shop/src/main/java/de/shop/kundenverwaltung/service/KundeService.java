package de.shop.kundenverwaltung.service;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.jboss.logging.Logger;

import de.shop.kundenverwaltung.domain.Kunde;
import de.shop.util.IdGroup;
import de.shop.util.Log;
import de.shop.util.Mock;
import de.shop.util.ValidatorProvider;

@Log
public class KundeService implements Serializable {
	private static final long serialVersionUID = 3188789767052580247L;
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	@Inject
	private ValidatorProvider validatorProvider;
	
	@PostConstruct
	private void postConstruct() {
		LOGGER.debugf("CDI-faehiges Bean %s wurde erzeugt", this);
	}
	
	@PreDestroy
	private void preDestroy() {
		LOGGER.debugf("CDI-faehiges Bean %s wird geloescht", this);
	}

	public Kunde findKundeById(Long id, Locale locale) {
		validateKundeId(id, locale);
		// TODO Datenbanzugriffsschicht statt Mock
		final Kunde kunde = Mock.findKundeById(id);
		return kunde;
	}
	
	private void validateKundeId(Long kundeId, Locale locale) {
		final Validator validator = validatorProvider.getValidator(locale);
		final Set<ConstraintViolation<Kunde>> violations = validator.validateValue(Kunde.class,
				                                                                           "id",
				                                                                           kundeId,
				                                                                           IdGroup.class);
		if (!violations.isEmpty())
			throw new InvalidKundeIdException(kundeId, violations);
	}
	
	public List<Kunde> findAllKunden() {
		// TODO Datenbanzugriffsschicht statt Mock
		final List<Kunde> kunden = Mock.findAllKunden();
		return kunden;
	}
	
	/**
	 */
	public List<Kunde> findKundenByNachname(String nachname, Locale locale) {
		validateNachname(nachname, locale);
		
		// TODO Datenbanzugriffsschicht statt Mock
		List<Kunde> kunden = Mock.findKundenByNachname(nachname);
		return kunden;
	}
	
	private void validateNachname(String nachname, Locale locale) {
		final Validator validator = validatorProvider.getValidator(locale);
		final Set<ConstraintViolation<Kunde>> violations = validator.validateValue(Kunde.class,
				                                                                           "nachname",
				                                                                           nachname,
				                                                                           Default.class);
		if (!violations.isEmpty())
			throw new InvalidNachnameException(nachname, violations);
	}

	public Kunde createKunde(Kunde kunde, Locale locale) {
		if (kunde == null) {
			return kunde;
		}

		// Werden alle Constraints beim Einfuegen gewahrt?
		validateKunde(kunde, locale, Default.class);

		// Pruefung, ob die Email-Adresse schon existiert
		// TODO Datenbanzugriffsschicht statt Mock
		if (Mock.findKundeByEmail(kunde.getEmail()) != null) {
			throw new EmailExistsException(kunde.getEmail());
		}

		kunde = Mock.createKunde(kunde);

		return kunde;
	}

	private void validateKunde(Kunde kunde, Locale locale, Class<?>... groups) {
		// Werden alle Constraints beim Einfuegen gewahrt?
		final Validator validator = validatorProvider.getValidator(locale);
		
		final Set<ConstraintViolation<Kunde>> violations = validator.validate(kunde, groups);
		if (!violations.isEmpty()) {
			throw new InvalidKundeException(kunde, violations);
		}
	}

	public Kunde updateKunde(Kunde kunde, Locale locale) {
		if (kunde == null) {
			return null;
		}

		// Werden alle Constraints beim Modifizieren gewahrt?
		validateKunde(kunde, locale, Default.class, IdGroup.class);

		// Pruefung, ob die Email-Adresse schon existiert
		final Kunde vorhandenerKunde = Mock.findKundeByEmail(kunde.getEmail());

		// Gibt es die Email-Adresse bei einem anderen, bereits vorhandenen Kunden?
		if (vorhandenerKunde.getId().longValue() != kunde.getId().longValue()) {
			throw new EmailExistsException(kunde.getEmail());
		}
		
		// TODO Datenbanzugriffsschicht statt Mock
		Mock.updateKunde(kunde);
		
		return kunde;
	}

	public void deleteKunde(Long kundeId, Locale locale) {
		validateKundeId(kundeId, locale);
		final Kunde kunde = findKundeById(kundeId, locale);
		if (kunde == null) {
			return;
		}

		// Gibt es Bestellungen?
		if (!kunde.getBestellungen().isEmpty()) {
			throw new KundeDeleteBestellungException(kunde);
		}
		
		// TODO Datenbanzugriffsschicht statt Mock
		Mock.deleteKunde(kunde);
	}
}

package de.shop.bestellverwaltung.service;

import static de.shop.util.Constants.KEINE_ID;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.bestellverwaltung.domain.Posten;
import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.kundenverwaltung.domain.Kunde;
import de.shop.kundenverwaltung.service.KundeService;
import de.shop.util.interceptor.Log;

@Dependent
@Log
public class BestellungServiceImpl implements Serializable, BestellungService {
	private static final long serialVersionUID = -9145947650157430928L;
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	@PersistenceContext
	private transient EntityManager em;
	
	@Inject
	private KundeService ks;
	
//	@Inject
//	private ValidatorProvider validatorProvider;
	
	@Inject
	@NeueBestellung
	private transient Event<Bestellung> event;
	
	@PostConstruct
	private void postConstruct() {
		LOGGER.debugf("CDI-faehiges Bean %s wurde erzeugt", this);
	}
	
	@PreDestroy
	private void preDestroy() {
		LOGGER.debugf("CDI-faehiges Bean %s wird geloescht", this);
	}
	
	/**
	 */
	@Override
	public Bestellung findBestellungById(Long id) {
		try {
			final Bestellung bestellung = em.find(Bestellung.class, id);
			return bestellung;
		}
		catch (NoResultException e) {
			return null;
		}
	}

	/**
	 */
	@Override
	public Kunde findKundeById(Long id) {
		try {
			final Kunde kunde = em.createNamedQuery(Bestellung.FIND_KUNDE_BY_ID, Kunde.class)
                                          .setParameter(Bestellung.PARAM_ID, id)
					                      .getSingleResult();
			return kunde;
		}
		catch (NoResultException e) {
			return null;
		}
	}

	/**
	 */
	@Override
	public List<Bestellung> findBestellungenByKunde(Kunde kunde) {
		if (kunde == null) {
			return Collections.emptyList();
		}
		final List<Bestellung> bestellungen = em.createNamedQuery(Bestellung.FIND_BESTELLUNGEN_BY_KUNDE,
                                                                  Bestellung.class)
                                                .setParameter(Bestellung.PARAM_KUNDE, kunde)
				                                .getResultList();
		return bestellungen;
	}



	
	
	/**
	 */
	@Override
	public List<Artikel> ladenhueter(int anzahl) {
		final List<Artikel> artikel = em.createNamedQuery(Posten.FIND_LADENHUETER, Artikel.class)
				                        .setMaxResults(anzahl)
				                        .getResultList();
		return artikel;
	}
	
	@Override
	public Bestellung updateBestellung(Bestellung bestellung) {
		if (bestellung == null) {
			return null;
		}
	

		em.merge(bestellung);
		return bestellung;
	}

	
	@Override
	public Bestellung createBestellung(Bestellung bestellung, String username) {
		if (bestellung == null) {
			return null;
		}

		// Den persistenten Kunden mit der transienten Bestellung verknuepfen
		final Kunde kunde = ks.findKundeByUserName(username);
		return createBestellung(bestellung, kunde);
	}
	
//	@Override
//	public Bestellung createBestellung(Bestellung bestellung, String email) {
//		if (bestellung == null) {
//			return null;
//		}
//
//		// Den persistenten Kunden mit der transienten Bestellung verknuepfen
//		final Kunde kunde = ks.findKundeByEmail(email);
//		return createBestellung(bestellung, kunde);
//	}
	
	@Override
	public Bestellung createBestellung(Bestellung bestellung, Kunde kunde) {
		if (bestellung == null || kunde == null) {
			return null;
		}

		// Den persistenten Kunden mit der transienten Bestellung verknuepfen
		if (!em.contains(kunde)) {
			kunde = ks.findKundeById(kunde.getId(), KundeService.FetchType.MIT_BESTELLUNGEN);
		}
		bestellung.setKunde(kunde);
		kunde.addBestellung(bestellung);
		
		// Vor dem Abspeichern IDs zuruecksetzen:
		// IDs koennten einen Wert != null haben, wenn sie durch einen Web Service uebertragen wurden
		bestellung.setId(KEINE_ID);
		for (Posten p : bestellung.getVieleposten()) {
			p.setId(KEINE_ID);
		}
		
		em.persist(bestellung);
		event.fire(bestellung);
		
		return bestellung;
	}
}

package de.shop.bestellverwaltung.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.artikelverwaltung.service.ArtikelService;
import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.bestellverwaltung.domain.Posten;
import de.shop.bestellverwaltung.service.BestellungService;
import de.shop.kundenverwaltung.domain.Kunde;
import de.shop.kundenverwaltung.rest.UriHelperKunde;
import de.shop.kundenverwaltung.service.KundeService;
import de.shop.kundenverwaltung.service.KundeService.FetchType;
import de.shop.util.LocaleHelper;
import de.shop.util.Log;
import de.shop.util.NotFoundException;
import de.shop.util.Transactional;

@Path("/bestellungen")
@Produces(APPLICATION_JSON)
@Consumes
@RequestScoped
@Transactional
@Log
public class BestellungResource {
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());

	@Context
	private UriInfo uriInfo;
	
	@Context
	private HttpHeaders headers;
	
	@Inject
	private BestellungService bs;
	
	@Inject
	private ArtikelService as;
	
	@Inject
	private KundeService ks;
	
	@Inject
	private UriHelperBestellung uriHelperBestellung;
	
	@Inject
	private UriHelperKunde uriHelperKunde;
	
	@Inject
	private LocaleHelper localeHelper;
	
	@PostConstruct
	private void postConstruct() {
		LOGGER.debugf("CDI-faehiges Bean %s wurde erzeugt", this);
	}
	
	@PreDestroy
	private void preDestroy() {
		LOGGER.debugf("CDI-faehiges Bean %s wird geloescht", this);
	}
	
	/**
	 * Mit der URL /bestellungen/{id} eine Bestellung ermitteln
	 * @param id ID der Bestellung
	 * @return Objekt mit Bestelldaten, falls die ID vorhanden ist
	 */
	@GET
	@Path("{id:[1-9][0-9]*}")
	public Bestellung findBestellungById(@PathParam("id") Long id) {
		final Bestellung bestellung = bs.findBestellungById(id);
		if (bestellung == null) {
			final String msg = "Keine Bestellung gefunden mit der ID " + id;
			throw new NotFoundException(msg);
		}

		// URLs innerhalb der gefundenen Bestellung anpassen
		uriHelperBestellung.updateUriBestellung(bestellung, uriInfo);
		return bestellung;
	}
	
//	@GET
//	@Path("{id:[1-9][0-9]*}")
//	public Bestellung findBestellungById(@PathParam("id") Long id) {
//		final Bestellung bestellung = bs.findBestellungById(id);
//		
//		// TODO Anwendungskern statt Mock, Verwendung von Locale
//		if (bestellung == null) {
//			throw new NotFoundException("Keine Bestellung mit der ID " + id + " gefunden.");
//		}
//		
//		// URLs innerhalb der gefundenen Bestellung anpassen
//		uriHelperBestellung.updateUriBestellung(bestellung, uriInfo);
//		return bestellung;
//	}
	
	/**
	 * Mit der URL /bestellungen/{id}/kunde den Kunden einer Bestellung ermitteln
	 * @param id ID der Bestellung
	 * @return Objekt mit Kundendaten, falls die ID vorhanden ist
	 */
	@GET
	@Path("{id:[1-9][0-9]*}/kunde")
	public Kunde findKundeByBestellungId(@PathParam("id") Long id) {
		final Kunde kunde = bs.findKundeById(id);
		if (kunde == null) {
			final String msg = "Keine Bestellung gefunden mit der ID " + id;
			throw new NotFoundException(msg);
		}

		// URLs innerhalb der gefundenen Bestellung anpassen
		uriHelperKunde.updateUriKunde(kunde, uriInfo);
		return kunde;
	}

	
	/**
	 * Mit der URL /bestellungen eine neue Bestellung anlegen
	 * @param bestellung die neue Bestellung
	 * @return Objekt mit Bestelldaten, falls die ID vorhanden ist
	 */
	
//	@POST
//	@Consumes(APPLICATION_JSON)
//	@Produces
//	public Response createBestellung(Bestellung bestellung) {
//		final Locale locale = localeHelper.getLocale(headers);
//		
//		// kundeId aus URI 
//		final URI kundeUri = bestellung.getKundeUri();
//		final String path = kundeUri.getPath();
//		final String idStr = path.substring(path.lastIndexOf('/') + 1);
//		final Long id = Long.parseLong(idStr);
//				
//		final Long kundeId = id;
//
//		final Kunde kunde = ks.findKundeById(kundeId, null, locale);
//
//		bestellung.setKunde(kunde);
//
//		bestellung = bs.createBestellung(bestellung, kunde, locale);
//
//		final URI bestellungUri = uriHelperBestellung.getUriBestellung(bestellung, uriInfo);
//		return Response.created(bestellungUri).build();
//	}
	@POST
	@Consumes(APPLICATION_JSON)
	@Produces
	public Response createBestellung(Bestellung bestellung) {
		// Schluessel des Kunden extrahieren
		final String kundeUriStr = bestellung.getKundeUri().toString();
		int startPos = kundeUriStr.lastIndexOf('/') + 1;
		final String kundeIdStr = kundeUriStr.substring(startPos);
		Long kundeId = null;
		try {
			kundeId = Long.valueOf(kundeIdStr);
		}
		catch (NumberFormatException e) {
			throw new NotFoundException("Kein Kunde vorhanden mit der ID " + kundeIdStr, e);
		}
		
		// persistente Artikel ermitteln
		final Collection<Posten> vieleposten = bestellung.getVieleposten();
		final List<Long> artikelIds = new ArrayList<>(vieleposten.size());
		for (Posten bp : vieleposten) {
			final String artikelUriStr = bp.getArtikelUri().toString();
			startPos = artikelUriStr.lastIndexOf('/') + 1;
			final String artikelIdStr = artikelUriStr.substring(startPos);
			Long artikelId = null;
			try {
				artikelId = Long.valueOf(artikelIdStr);
			}
			catch (NumberFormatException e) {
				// Ungueltige Artikel-ID: wird nicht beruecksichtigt
				continue;
			}
			artikelIds.add(artikelId);
		}
		
		if (artikelIds.isEmpty()) {
			// keine einzige gueltige Artikel-ID
			final StringBuilder sb = new StringBuilder("Keine Artikel vorhanden mit den IDs: ");
			for (Posten bp : vieleposten) {
				final String artikelUriStr = bp.getArtikelUri().toString();
				startPos = artikelUriStr.lastIndexOf('/') + 1;
				sb.append(artikelUriStr.substring(startPos));
				sb.append(' ');
			}
			throw new NotFoundException(sb.toString());
		}

		final Collection<Artikel> gefundeneArtikel = as.findArtikelByIds(artikelIds);
		if (gefundeneArtikel.isEmpty()) {
			throw new NotFoundException("Keine Artikel vorhanden mit den IDs: " + artikelIds);
		}
		
		// Bestellpositionen haben URLs fuer persistente Artikel.
		// Diese persistenten Artikel wurden in einem DB-Zugriff ermittelt (s.o.)
		// Fuer jede Bestellposition wird der Artikel passend zur Artikel-URL bzw. Artikel-ID gesetzt.
		// Bestellpositionen mit nicht-gefundene Artikel werden eliminiert.
		int i = 0;
		final List<Posten> neueBestellpositionen = new ArrayList<>(vieleposten.size());
		for (Posten p : vieleposten) {
			// Artikel-ID der aktuellen Bestellposition (s.o.):
			// artikelIds haben gleiche Reihenfolge wie bestellpositionen
			final long artikelId = artikelIds.get(i++);
			
			// Wurde der Artikel beim DB-Zugriff gefunden?
			for (Artikel artikel : gefundeneArtikel) {
				if (artikel.getId().longValue() == artikelId) {
					// Der Artikel wurde gefunden
					p.setArtikel(artikel);
					neueBestellpositionen.add(p);
					break;					
				}
			}
		}
		bestellung.setVieleposten(neueBestellpositionen);
		
		final Locale locale = localeHelper.getLocale(headers);
		bestellung = bs.createBestellung(bestellung, kundeId, locale);

		final URI bestellungUri = uriHelperBestellung.getUriBestellung(bestellung, uriInfo);
		final Response response = Response.created(bestellungUri).build();
		LOGGER.fatal(bestellungUri.toString());
		
		return response;
	}
	
	@PUT
	@Consumes(APPLICATION_JSON)
	@Produces
	public void updateBestellung(Bestellung bestellung) {
		final Locale locale = localeHelper.getLocale(headers);

		final Bestellung origBestellung = bs.findBestellungById(bestellung.getId());
		if (origBestellung == null) {
			// TODO msg passend zu locale
			final String msg = "Kein Kunde gefunden mit der ID " + bestellung.getId();
			throw new NotFoundException(msg);
		}
		LOGGER.tracef("Bestellung vorher: %s", origBestellung);
	
		// Daten des vorhandenen Kunden ueberschreiben
		origBestellung.setValues(bestellung);
		LOGGER.tracef("Bestellung nachher: %s", origBestellung);
		
		// Update durchfuehren
		bestellung = bs.updateBestellung(origBestellung, locale);
		if (bestellung == null) {
			// TODO msg passend zu locale
			final String msg = "Kein Kunde gefunden mit der ID " + origBestellung.getId();
			throw new NotFoundException(msg);
		}
//		// kundeId aus URI 
//		final URI kundeUri = bestellung.getKundeUri();
//		final String path = kundeUri.getPath();
//		final String idStr = path.substring(path.lastIndexOf('/') + 1);
//		final Long id = Long.parseLong(idStr);
//				
//		final Long kundeId = id;
//
//		final Kunde kunde = ks.findKundeById(kundeId, null, locale);
//
//		bestellung.setKunde(kunde);
//		
//		bestellung = bs.updateBestellung(bestellung, locale);
//		return Response.noContent().build();
	}
}

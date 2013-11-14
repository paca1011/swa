package de.shop.bestellverwaltung.rest;

import static de.shop.util.TestConstants.ARTIKEL_URI;
import static de.shop.util.TestConstants.KUNDEN_URI;
import static de.shop.util.TestConstants.BESTELLUNGEN_ID_KUNDE_URI;
import static de.shop.util.TestConstants.BESTELLUNGEN_ID_PATH_PARAM;
import static de.shop.util.TestConstants.BESTELLUNGEN_ID_URI;
import static de.shop.util.TestConstants.BESTELLUNGEN_URI;
import static de.shop.util.TestConstants.KUNDEN_ID_URI;
import static de.shop.util.TestConstants.PASSWORD;
import static de.shop.util.TestConstants.USERNAME;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.fest.assertions.api.Assertions.assertThat;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.bestellverwaltung.domain.Posten;
import de.shop.kundenverwaltung.domain.Adresse;
import de.shop.kundenverwaltung.domain.Kunde;
import de.shop.kundenverwaltung.rest.KundeResource;
import de.shop.util.AbstractResourceTest;


//Logging durch java.util.logging
/**
 * @author <a href="mailto:stto1016@HS-Karlsruhe.de">Tobias Steiner</a>
 */

@RunWith(Arquillian.class)
public class BestellungResourceTest extends AbstractResourceTest {
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	
	private static final Long BESTELLUNG_ID_VORHANDEN = Long.valueOf(400);
	private static final Long ARTIKEL_ID_VORHANDEN_1 = Long.valueOf(300);
	private static final Long ARTIKEL_ID_VORHANDEN_2 = Long.valueOf(301);
	private static final Long KUNDE_ID_VORHANDEN = Long.valueOf(101);
	
	private static final Integer NEUE_VERSION = 0;
	private static final String NEUER_NACHNAME = "Nachnameneu";
	private static final String NEUER_VORNAME = "Vorname";
	private static final String NEUE_EMAIL = NEUER_NACHNAME + "@test.de";
	private static final String NEUES_GESCHLECHT = "m";
	private static final String NEUE_PLZ = "76133";
	private static final String NEUE_STADT = "Karlsruhe";
	private static final String NEUE_STRASSE = "Testweg";
	private static final String NEUE_HAUSNUM = "1";
	private static final String NEUES_PASSWORT = "neuesPassword";
	private static final Long KUNDE_ID_UPDATE = Long.valueOf(103);
	private static final Long KUNDE_ID_DELETE = Long.valueOf(105);

	private static final Long BESTELLUNG_ID_NICHT_VORHANDEN = Long.valueOf(414);
	
	@Test
	@InSequence(1)
	public void validate() {
		assertThat(true).isTrue();
	}
	
	@Test
	@InSequence(2)
	public void findBestellungById() {
		LOGGER.finer("BEGINN");
		
		// Given
		final Long bestellungId = BESTELLUNG_ID_VORHANDEN;
		
		// When
		final Response response = getHttpsClient().target(BESTELLUNGEN_ID_URI)
                                                  .resolveTemplate(BESTELLUNGEN_ID_PATH_PARAM, bestellungId)
                                                  .request()
                                                  .accept(APPLICATION_JSON)
                                                  .get();
		
		// Then
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		final Bestellung bestellung = response.readEntity(Bestellung.class);
		
		assertThat(bestellung.getId()).isEqualTo(bestellungId);
		assertThat(bestellung.getVieleposten()).isNotEmpty();

		LOGGER.finer("ENDE");
	}

	@Test
	@InSequence(3)
	public void findKundeByBestellungId() {
		LOGGER.finer("BEGINN");
		
		// Given
		final Long bestellungId = BESTELLUNG_ID_VORHANDEN;
		
		// When
		Response response = getHttpsClient().target(BESTELLUNGEN_ID_KUNDE_URI)
                                            .resolveTemplate(BESTELLUNGEN_ID_PATH_PARAM, bestellungId)
                                            .request()
                                            .accept(APPLICATION_JSON)
                                            .get();
			
		// Then
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		final Kunde kunde = response.readEntity(Kunde.class);
		assertThat(kunde).isNotNull();
		
		response = getHttpsClient().target(KUNDEN_ID_URI)
                                   .resolveTemplate(KundeResource.KUNDEN_ID_PATH_PARAM, kunde.getId())
                                   .request()
                                   .accept(APPLICATION_JSON)
                                   .get();
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		assertThat(response.getLinks()).isNotEmpty();
		response.close();    // response.readEntity() wurde nicht aufgerufen

		LOGGER.finer("ENDE");
	}
	
	
	@Ignore
	@Test
	@InSequence(4)
	public void findBestellungByIdNichtVorhanden() {
		LOGGER.finer("BEGINN");
		
		// Given
		final Long bestellungId = BESTELLUNG_ID_NICHT_VORHANDEN;
		
		// When
		final Response response = getHttpsClient().target(BESTELLUNGEN_ID_URI)
                                                  .resolveTemplate(BESTELLUNGEN_ID_PATH_PARAM, bestellungId)
                                                  .request()
                                                  .accept(APPLICATION_JSON)
                                                  .get();
		
		// Then
		assertThat(response.getStatus()).isEqualTo(HTTP_NOT_FOUND);
    	final String fehlermeldung = response.readEntity(String.class);
    	assertThat(fehlermeldung).startsWith("Keine Bestellung mit der ID")
    	                         .endsWith("gefunden.");

		LOGGER.finer("ENDE");
	}
	
	@Test
	@InSequence(10)
	public void createBestellung() throws URISyntaxException {
		LOGGER.finer("BEGINN");
		
		
		// Given
		final Long artikelId1 = ARTIKEL_ID_VORHANDEN_1;
		final Long artikelId2 = ARTIKEL_ID_VORHANDEN_2;
		final Long kundeId = KUNDE_ID_VORHANDEN;
		
		// Neues, client-seitiges Bestellungsobjekt als JSON-Datensatz
		final Bestellung bestellung = new Bestellung();
		
		Posten p = new Posten();
		p.setArtikelUri(new URI(ARTIKEL_URI + "/" + artikelId1));
		p.setAnzahl((short) 1);
		p.setVersion(0);
		bestellung.addVieleposten(p);

		p = new Posten();
		p.setArtikelUri(new URI(ARTIKEL_URI + "/" + artikelId2));
		p.setAnzahl((short) 1);
		p.setVersion(0);
		bestellung.addVieleposten(p);
		
		bestellung.setAusgeliefert(Integer.valueOf(0));
		bestellung.setGesamtpreis(BigDecimal.valueOf(100));
		bestellung.setStatus("Ausgeliefert");
		bestellung.setKundeUri(new URI(KUNDEN_URI + "/" + kundeId));
		bestellung.setVersion(0);
		bestellung.setLieferantUri(null);
		
		
		// When
		Long id;
		Response response = getHttpsClient(USERNAME, PASSWORD).target(BESTELLUNGEN_URI)
                                                              .request()
                                                              .post(json(bestellung));
			
		// Then
		assertThat(response.getStatus()).isEqualTo(HTTP_CREATED);
		final String location = response.getLocation().toString();
		response.close();
			
		final int startPos = location.lastIndexOf('/');
		final String idStr = location.substring(startPos + 1);
		id = Long.valueOf(idStr);
		assertThat(id).isPositive();
		
		// Gibt es die neue Bestellung?
		response = getHttpsClient().target(BESTELLUNGEN_ID_URI)
                                   .resolveTemplate(BESTELLUNGEN_ID_PATH_PARAM, id)
                                   .request()
                                   .accept(APPLICATION_JSON)
                                   .get();
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		response.close();
		
		LOGGER.finer("ENDE");
	}
	
	
	@Ignore
	@Test
	@InSequence(30)
	public void updateBestellung() {
		LOGGER.finer("BEGINN");
		
		// Given
		final Long bestellungId = BESTELLUNG_ID_VORHANDEN;
		
		// When
		Response response = getHttpsClient().target(BESTELLUNGEN_ID_URI)
                                                  .resolveTemplate(BESTELLUNGEN_ID_PATH_PARAM, bestellungId)
                                                  .request()
                                                  .accept(APPLICATION_JSON)
                                                  .get();
		
		// Then
		Bestellung bestellung = response.readEntity(Bestellung.class);
		assertThat(bestellung.getId()).isEqualTo(bestellungId);

		//ausgeliefert auf 1 setzen
		bestellung.setAusgeliefert(1);
		
		response = getHttpsClient(USERNAME, PASSWORD).target(BESTELLUNGEN_URI)
                .request()
                .put(json(bestellung));
		// Then
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		bestellung = response.readEntity(Bestellung.class);
		
		LOGGER.finer("ENDE");
	}
}

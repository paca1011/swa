package de.shop.kundenverwaltung.rest;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

import java.lang.invoke.MethodHandles;
import java.util.Locale;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.shop.kundenverwaltung.domain.Adresse;
import de.shop.kundenverwaltung.domain.Kunde;
import de.shop.util.AbstractResourceTest;
import static de.shop.util.TestConstants.USERNAME;
import static de.shop.util.TestConstants.PASSWORD;
import static de.shop.util.TestConstants.KUNDEN_URI;

import static javax.ws.rs.client.Entity.json;


//Logging durch java.util.logging
/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
@RunWith(Arquillian.class)
public class KundeResourceTest extends AbstractResourceTest {
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	
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
//	private static final Long ARTIKEL_ID_VORHANDEN = Long.valueOf(300);
	
	@Test
	@InSequence(1)
	public void validate() {
		assertThat(true).isTrue();
	}
	
	@Ignore
	@Test
	@InSequence(2)
	public void beispielIgnore() {
		assertThat(true).isFalse();
	}
	
	@Ignore
	@Test
	@InSequence(3)
	public void beispielFailMitIgnore() {
		fail("Beispiel fuer fail()");
	}
	
	@Test
	@InSequence(4)
	public void findKundeById() {
		LOGGER.finer("BEGINN");
		
		// Given
		final Long kundeId = Long.valueOf(101);
		
		// When
		Response response = ClientBuilder.newClient()
									.target("http://localhost:8080/shop/rest/kunden/{id}")
									.resolveTemplate("id", kundeId)
									.request()
									.accept(APPLICATION_JSON)
									.acceptLanguage(Locale.GERMAN)
									.get();
	
		// Then
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		Kunde kunde = response.readEntity(Kunde.class);
		assertThat(kunde.getId()).isEqualTo(kundeId);		
		
		LOGGER.finer("ENDE");
	}
	
	@Test
	@InSequence(5)
	public void findKundeByIdNichtVorhanden() {
		LOGGER.finer("BEGINN");
		
		// Given
		final Long kundeId = Long.valueOf(333);
		
		// When
		Response response = ClientBuilder.newClient()
									.target("http://localhost:8080/shop/rest/kunden/{id}")
									.resolveTemplate("id", kundeId)
									.request()
									.accept(APPLICATION_JSON)
									.acceptLanguage(Locale.GERMAN)
									.get();
	
		// Then
		assertThat(response.getStatus()).isEqualTo(HTTP_NOT_FOUND);
		Kunde kunde = response.readEntity(Kunde.class);
		assertThat(kunde.getId()).isEqualTo(kundeId);		
		
		LOGGER.finer("ENDE");
	}

	@Test
	@InSequence(6)
	public void createKunde() {
		LOGGER.finer("BEGINN");
		
		// Given
		final String nachname = NEUER_NACHNAME;
		final String vorname = NEUER_VORNAME;
		final String email = NEUE_EMAIL;
		final String geschlecht = NEUES_GESCHLECHT;
		final String passwort = NEUES_PASSWORT;
		final Integer version = NEUE_VERSION;
		final String plz = NEUE_PLZ;
		final String stadt = NEUE_STADT;
		final String strasse = NEUE_STRASSE;
		final String hausnum = NEUE_HAUSNUM;
		
		final Kunde kunde = new Kunde();
		kunde.setVorname(vorname);
		kunde.setNachname(nachname);
		kunde.setEmail(email);
		kunde.setGeschlecht(geschlecht);
		kunde.setPasswort(passwort);
		kunde.setVersion(version);

		final Adresse adresse = new Adresse();
		adresse.setStadt(stadt);
		adresse.setHausnum(hausnum);
		adresse.setPlz(plz);
		adresse.setStrasse(strasse);
		kunde.setAdresse(adresse);
		
		Response response = getHttpsClient(USERNAME, PASSWORD).target(KUNDEN_URI)
                                                              .request()
                                                              .post(json(kunde));
			
		// Then
		assertThat(response.getStatus()).isEqualTo(HTTP_CREATED);
		String location = response.getLocation().toString();
		response.close();
		
		final int startPos = location.lastIndexOf('/');
		final String idStr = location.substring(startPos + 1);
		final Long id = Long.valueOf(idStr);
		assertThat(id).isPositive();

		LOGGER.finer("ENDE");
	}


}

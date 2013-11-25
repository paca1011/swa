package de.shop.kundenverwaltung.rest;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.Locale.GERMAN;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.client.Entity.json;
import de.shop.kundenverwaltung.domain.Adresse;
import de.shop.kundenverwaltung.domain.Kunde;
import de.shop.util.AbstractResourceTest;
import static de.shop.util.TestConstants.KUNDEN_ID_FILE_URI;
import static de.shop.util.TestConstants.USERNAME;
import static de.shop.util.TestConstants.PASSWORD;
import static de.shop.util.TestConstants.USERNAME_ADMIN;
import static de.shop.util.TestConstants.PASSWORD_ADMIN;
import static de.shop.util.TestConstants.KUNDEN_URI;
import static de.shop.util.TestConstants.KUNDEN_ID_URI;


//Logging durch java.util.logging
/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
@RunWith(Arquillian.class)
public class KundeResourceTest extends AbstractResourceTest {
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	
	private static final String IMAGE_FILENAME = "image.png";
	private static final String IMAGE_PATH_UPLOAD = "src/test/resources/rest/" + IMAGE_FILENAME;
	private static final String IMAGE_MIMETYPE = "image/png";
	private static final Long KUNDE_ID_UPLOAD = Long.valueOf(102);
	
	private static final Long KUNDE_ID_DELETE = Long.valueOf(106);
	
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
		final Response response = getHttpsClient().target(KUNDEN_ID_URI)
                								  .resolveTemplate(KundeResource.KUNDEN_ID_PATH_PARAM, kundeId)
                								  .request()
                								  .acceptLanguage(GERMAN)
                								  .get();
	
		// Then
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		
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
		
		final Response response = getHttpsClient(USERNAME, PASSWORD).target(KUNDEN_URI)
                                                              .request()
                                                              .post(json(kunde));
			
		// Then
		assertThat(response.getStatus()).isEqualTo(HTTP_CREATED);
		final String location = response.getLocation().toString();
		response.close();
		
		final int startPos = location.lastIndexOf('/');
		final String idStr = location.substring(startPos + 1);
		final Long id = Long.valueOf(idStr);
		assertThat(id).isPositive();

		LOGGER.finer("ENDE");
	}

	@Test
	@InSequence(7)
	public void updateKunde() {
		LOGGER.finer("BEGINN");
		
		// Given
		final Long kundeId = KUNDE_ID_UPDATE;
		final String neuesPasswort = NEUES_PASSWORT;
		
		// When
		Response response = ClientBuilder.newClient()
						.target("http://localhost:8080/shop/rest/kunden/{id}")
						.resolveTemplate("id", kundeId)
						.request()
						.accept(APPLICATION_JSON)
						.acceptLanguage(GERMAN)
						.get();
		
		Kunde kunde = response.readEntity(Kunde.class);
		assertThat(kunde.getId()).isEqualTo(kundeId);

		// Aus den gelesenen JSON-Werten ein neues JSON-Objekt mit neuem Nachnamen bauen
		kunde.setPasswort(neuesPasswort);
		
		response = getHttpsClient(USERNAME, PASSWORD).target(KUNDEN_URI)
						.request()
						.accept(APPLICATION_JSON)
						.put(json(kunde));
		// Then
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		kunde = response.readEntity(Kunde.class);
		
		LOGGER.finer("ENDE");
	}
	
	@Test
	@InSequence(8)
	public void uploadDownload() throws IOException {
	
		// Given
				final Long kundeId = KUNDE_ID_UPLOAD;
				final String path = IMAGE_PATH_UPLOAD;
				final String mimeType = IMAGE_MIMETYPE;
				
		// Datei einlesen
		final byte[] uploadBytes = Files.readAllBytes(Paths.get(path));
				
		// When
		final Response response = getHttpsClient(USERNAME, PASSWORD).target(KUNDEN_ID_FILE_URI)
		                                                      .resolveTemplate(KundeResource.KUNDEN_ID_PATH_PARAM,
		                                                         		           kundeId)
		                                                      .request()
		                                                      .post(entity(uploadBytes, mimeType));
		
		// Then
		assertThat(response.getStatus()).isEqualTo(HTTP_CREATED);
		// id extrahieren aus http://localhost:8080/shop/rest/kunden/<id>/file
		final String location = response.getLocation().toString();
		response.close();
		
		final String idStr = location.replace(KUNDEN_URI + '/', "")
                .replace("/file", "");
		assertThat(idStr).isEqualTo(kundeId.toString());
		
		LOGGER.finer("ENDE");
	}
	
	@Test
	@InSequence(9)
	public void deleteKunde() {
		LOGGER.finer("BEGINN");
		
		// Given
		final Long kundeId = KUNDE_ID_DELETE;
		
		// When
		Response response = getHttpsClient().target(KUNDEN_ID_URI)
                                            .resolveTemplate(KundeResource.KUNDEN_ID_PATH_PARAM, kundeId)
                                            .request()
                                            .accept(APPLICATION_JSON)
                                            .get();
		
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		response.close();
		
		response = getHttpsClient(USERNAME_ADMIN, PASSWORD_ADMIN).target(KUNDEN_ID_URI)
                                                                 .resolveTemplate
                                                                 (KundeResource.KUNDEN_ID_PATH_PARAM, kundeId)                                                             		         
                                                                 .request()
                                                                 .delete();
		
		// Then
		assertThat(response.getStatus()).isEqualTo(HTTP_NO_CONTENT);
		response.close();

		LOGGER.finer("ENDE");
	}
	
	}


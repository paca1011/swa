package de.shop.kundenverwaltung.rest;

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

import de.shop.kundenverwaltung.domain.Kunde;
import de.shop.util.AbstractResourceTest;


//Logging durch java.util.logging
/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
@RunWith(Arquillian.class)
public class KundeResourceTest extends AbstractResourceTest {
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	
	private static final String NACHNAME_VORHANDEN = "Admin";
	
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
	@InSequence(4)
	@Ignore
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
	@InSequence(5)
	@Ignore
	public void findKundeByNachname() {
		LOGGER.finer("BEGINN");
		
		// Given
		final String kundeNachname = NACHNAME_VORHANDEN;
		
		// When
		Response response = ClientBuilder.newClient()
									.target("http://localhost:8080/shop/rest/kunden/prefix/nachname/{nachname}")
									.resolveTemplate("nachname", kundeNachname)
									.request()
									.accept(APPLICATION_JSON)
									.acceptLanguage(Locale.GERMAN)
									.get();
	
		// Then
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		Kunde kunde = response.readEntity(Kunde.class);
		assertThat(kunde.getNachname()).isEqualTo(kundeNachname);		
		
		LOGGER.finer("ENDE");
	}

}

package de.shop.artikelverwaltung.rest;

import static de.shop.util.TestConstants.ARTIKEL_URI;
import static de.shop.util.TestConstants.ARTIKEL_ID_PATH_PARAM;
import static de.shop.util.TestConstants.ARTIKEL_ID_URI;
import static de.shop.util.TestConstants.PASSWORD;
import static de.shop.util.TestConstants.USERNAME;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.Locale.GERMAN;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.fest.assertions.api.Assertions.assertThat;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.util.AbstractResourceTest;

@RunWith(Arquillian.class)
public class ArtikelResourceTest extends AbstractResourceTest {
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	
	private static final Long ARTIKEL_ID_VORHANDEN = Long.valueOf(300);
	private static final Long ARTIKEL_ID_UPDATE = Long.valueOf(302);
	
	private static final String NEUE_BEZEICHNUNG = "Testtisch";
	private static final String NEUE_FARBE = "Testfarbe";
	private static final BigDecimal NEUER_PREISKUNDE = new BigDecimal(25);
	private static final BigDecimal NEUER_PREISLIEFERANT = new BigDecimal(22);
	private static final Long NEUER_BESTAND = new Long(100);
	
	@Test
	@InSequence(1)
	public void validate() {
		assertThat(true).isTrue();
	}
	
	
	
	@Test
	@InSequence(10)
	public void findArtikelById() {
		LOGGER.finer("BEGINN");
		
		// Given
		final Long artikelId = ARTIKEL_ID_VORHANDEN;
		
		// When
		final Response response = getHttpsClient().target(ARTIKEL_ID_URI)
                                                  .resolveTemplate(ARTIKEL_ID_PATH_PARAM, artikelId)
                                                  .request()
                                                  .acceptLanguage(GERMAN)
                                                  .get();
		
		// Then
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		final Artikel artikel = response.readEntity(Artikel.class);
		
		assertThat(artikel.getId()).isEqualTo(artikelId);

		LOGGER.finer("ENDE");
	}
	
	
	
	@Test
	@InSequence(20)
	public void createArtikel() {
		LOGGER.finer("BEGINN");
	
		// Given
		final String bezeichnung = NEUE_BEZEICHNUNG;
		final String farbe = NEUE_FARBE;
		final BigDecimal preisKunde = NEUER_PREISKUNDE;
		final BigDecimal preisLieferant = NEUER_PREISLIEFERANT;
		final Long bestand = NEUER_BESTAND;
		
		final Artikel artikel = new Artikel();
		artikel.setBezeichnung(bezeichnung);
		artikel.setFarbe(farbe);
		artikel.setPreisKunde(preisKunde);
		artikel.setPreisLieferant(preisLieferant);
		artikel.setBestand(bestand);
		
		
		final Response response = getHttpsClient(USERNAME, PASSWORD).target(ARTIKEL_URI)
                                                              .request()
                                                              .post(json(artikel));
			
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
	@InSequence(30)
	public void updateArtikel() {
		LOGGER.finer("BEGINN");
		
		// Given
		final Long artikelId = ARTIKEL_ID_UPDATE;
		final Long neuerBestand = NEUER_BESTAND;
		
		// When
		Response response = getHttpsClient().target(ARTIKEL_ID_URI)
								.resolveTemplate(ARTIKEL_ID_PATH_PARAM, artikelId)
								.request()
								.acceptLanguage(GERMAN)
								.get();
		
		Artikel artikel = response.readEntity(Artikel.class);
		assertThat(artikel.getId()).isEqualTo(artikelId);

		// Aus den gelesenen JSON-Werten ein neues JSON-Objekt mit neuem Bestand bauen
		artikel.setBestand(neuerBestand);
		
		response = getHttpsClient(USERNAME, PASSWORD).target(ARTIKEL_URI)
						.request()
						.accept(APPLICATION_JSON)
						.put(json(artikel));
		// Then
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		artikel = response.readEntity(Artikel.class);
		
		LOGGER.finer("ENDE");
	}

}

package de.shop.artikelverwaltung.rest;

import static de.shop.util.TestConstants.ARTIKEL_URI;
import static de.shop.util.TestConstants.ARTIKEL_ID_PATH_PARAM;
import static java.net.HttpURLConnection.HTTP_OK;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.fest.assertions.api.Assertions.assertThat;

import java.lang.invoke.MethodHandles;
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
		final Response response = getHttpsClient().target(ARTIKEL_URI)
												  .resolveTemplate(ARTIKEL_ID_PATH_PARAM, artikelId)
                                                  .request()
                                                  .accept(APPLICATION_JSON)
                                                  .get();
		
		// Then
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		final Artikel artikel = response.readEntity(Artikel.class);
		
		assertThat(artikel.getId()).isEqualTo(artikelId);

		LOGGER.finer("ENDE");
	}

}
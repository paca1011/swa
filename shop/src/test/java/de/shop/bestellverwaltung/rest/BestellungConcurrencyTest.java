package de.shop.bestellverwaltung.rest;

import static de.shop.util.TestConstants.BESTELLUNGEN_ID_PATH_PARAM;
import static de.shop.util.TestConstants.BESTELLUNGEN_ID_URI;
import static de.shop.util.TestConstants.BESTELLUNGEN_URI;
import static de.shop.util.TestConstants.PASSWORD;
import static de.shop.util.TestConstants.USERNAME;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.fest.assertions.api.Assertions.assertThat;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.util.AbstractResourceTest;
import de.shop.util.HttpsConcurrencyHelper;


//Logging durch java.util.logging
/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
@RunWith(Arquillian.class)
public class BestellungConcurrencyTest extends AbstractResourceTest {
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	
	private static final long TIMEOUT = 5;

	private static final Long BESTELLUNG_ID_UPDATE = Long.valueOf(401);
	private static final Long BESTELLUNG_ID_DELETE = Long.valueOf(402);
	private static final BigDecimal NEUER_GESAMTPREIS = BigDecimal.valueOf(200);
	private static final BigDecimal NEUER_GESAMTPREIS_2 = BigDecimal.valueOf(300);


	@Test
	@InSequence(1)
	public void updateUpdate() throws InterruptedException, ExecutionException, TimeoutException {
		LOGGER.finer("BEGINN");
		
		// Given
		final Long bestellungId = BESTELLUNG_ID_UPDATE;
    	final BigDecimal neuerGesamtpreis2 = NEUER_GESAMTPREIS_2;
		
		// When
		final Response response = getHttpsClient().target(BESTELLUNGEN_ID_URI)
										                .resolveTemplate(BESTELLUNGEN_ID_PATH_PARAM, bestellungId)
										                .request()
										                .accept(APPLICATION_JSON)
										                .get();

    	final Bestellung bestellung = response.readEntity(Bestellung.class);

    	// Konkurrierendes Update
		// Aus den gelesenen JSON-Werten ein neues JSON-Objekt mit neuem Nachnamen bauen
		bestellung.setGesamtpreis(neuerGesamtpreis2);
		
		final Callable<Integer> concurrentUpdate = new Callable<Integer>() {
			@Override
			public Integer call() {
				final Response response = new HttpsConcurrencyHelper()
				                          .getHttpsClient(USERNAME, PASSWORD)
                                          .target(BESTELLUNGEN_URI)
                                          .request()
                                          .accept(APPLICATION_JSON)
                                          .put(json(bestellung));
				final int status = response.getStatus();
				response.close();
				return Integer.valueOf(status);
			}
		};
    	final Integer status = Executors.newSingleThreadExecutor()
    			                        .submit(concurrentUpdate)
    			                        .get(TIMEOUT, SECONDS);   // Warten bis der "parallele" Thread fertig ist
		assertThat(status.intValue()).isEqualTo(HTTP_OK);
		
		LOGGER.finer("ENDE");
	}
	
	
	
	@Test
	@InSequence(3)
	public void deleteUpdate() throws InterruptedException, ExecutionException, TimeoutException {
		LOGGER.finer("BEGINN");
		
		// Given
		final Long bestellungId = BESTELLUNG_ID_DELETE;
    	final BigDecimal neuerGesamtpreis = NEUER_GESAMTPREIS;
		
		// When
		final Response response = getHttpsClient().target(BESTELLUNGEN_ID_URI)
                .resolveTemplate(BESTELLUNGEN_ID_PATH_PARAM, bestellungId)
                .request()
                .accept(APPLICATION_JSON)
                .get();


		final Bestellung bestellung = response.readEntity(Bestellung.class);

		// Konkurrierendes Update
		bestellung.setGesamtpreis(neuerGesamtpreis);
    	final Callable<Integer> concurrentUpdate = new Callable<Integer>() {
			@Override
			public Integer call() {
				final Response response = new HttpsConcurrencyHelper()
				                          .getHttpsClient(USERNAME, PASSWORD)
                                          .target(BESTELLUNGEN_URI)
                                          .request()
                                          .accept(APPLICATION_JSON)
                                          .put(json(bestellung));
				final int status = response.getStatus();
				response.close();
				return Integer.valueOf(status);
			}
		};
    	final Integer status = Executors.newSingleThreadExecutor()
    			                        .submit(concurrentUpdate)
    			                        .get(TIMEOUT, SECONDS);   // Warten bis der "parallele" Thread fertig ist
		assertThat(status.intValue()).isEqualTo(HTTP_OK);
		
		LOGGER.finer("ENDE");
	}
}

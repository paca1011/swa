package de.shop.kundenverwaltung.rest;

import static de.shop.util.TestConstants.KUNDEN_ID_URI;
import static de.shop.util.TestConstants.KUNDEN_URI;
import static de.shop.util.TestConstants.PASSWORD;
import static de.shop.util.TestConstants.USERNAME;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.fest.assertions.api.Assertions.assertThat;

import java.lang.invoke.MethodHandles;
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

import de.shop.kundenverwaltung.domain.Kunde;
import de.shop.util.AbstractResourceTest;
import de.shop.util.HttpsConcurrencyHelper;


//Logging durch java.util.logging
/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
@RunWith(Arquillian.class)
public class KundeResourceConcurrencyTest extends AbstractResourceTest {
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	
	private static final long TIMEOUT = 5;

	private static final Long KUNDE_ID_UPDATE = Long.valueOf(101);
	private static final String NEUER_NACHNAME = "Testname";
	private static final String NEUER_NACHNAME_2 = "Neuername";
	private static final Long KUNDE_ID_DELETE2 = Long.valueOf(103);

	@Test
	@InSequence(1)
	public void updateUpdate() throws InterruptedException, ExecutionException, TimeoutException {
		LOGGER.finer("BEGINN");
		
		// Given
		final Long kundeId = KUNDE_ID_UPDATE;
    	final String neuerNachname2 = NEUER_NACHNAME_2;
		
		// When
		final Response response = getHttpsClient().target(KUNDEN_ID_URI)
                                            .resolveTemplate(KundeResource.KUNDEN_ID_PATH_PARAM, kundeId)
                                            .request()
                                            .accept(APPLICATION_JSON)
                                            .get();

    	final Kunde kunde = response.readEntity(Kunde.class);

    	// Konkurrierendes Update
		// Aus den gelesenen JSON-Werten ein neues JSON-Objekt mit neuem Nachnamen bauen
		kunde.setNachname(neuerNachname2);
		
		final Callable<Integer> concurrentUpdate = new Callable<Integer>() {
			@Override
			public Integer call() {
				final Response response = new HttpsConcurrencyHelper()
				                          .getHttpsClient(USERNAME, PASSWORD)
                                          .target(KUNDEN_URI)
                                          .request()
                                          .accept(APPLICATION_JSON)
                                          .put(json(kunde));
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
		final Long kundeId = KUNDE_ID_DELETE2;
    	final String neuerNachname = NEUER_NACHNAME;
		
		// When
		final Response response = getHttpsClient().target(KUNDEN_ID_URI)
                                            .resolveTemplate(KundeResource.KUNDEN_ID_PATH_PARAM, kundeId)
                                            .request()
                                            .accept(APPLICATION_JSON)
                                            .get();

		final Kunde kunde = response.readEntity(Kunde.class);

		// Konkurrierendes Update
		kunde.setNachname(neuerNachname);
    	final Callable<Integer> concurrentUpdate = new Callable<Integer>() {
			@Override
			public Integer call() {
				final Response response = new HttpsConcurrencyHelper()
				                          .getHttpsClient(USERNAME, PASSWORD)
                                          .target(KUNDEN_URI)
                                          .request()
                                          .accept(APPLICATION_JSON)
                                          .put(json(kunde));
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

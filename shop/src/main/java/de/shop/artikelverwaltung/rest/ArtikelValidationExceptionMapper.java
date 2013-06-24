package de.shop.artikelverwaltung.rest;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.CONFLICT;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.artikelverwaltung.service.AbstractArtikelValidationException;
import de.shop.util.Log;


@Provider
@ApplicationScoped
@Log
public class ArtikelValidationExceptionMapper implements ExceptionMapper<AbstractArtikelValidationException> {
	private static final String NEWLINE = System.getProperty("line.separator");

	@Override
	public Response toResponse(AbstractArtikelValidationException e) {
		final Collection<ConstraintViolation<Artikel>> violations = e.getViolations();
		final StringBuilder sb = new StringBuilder();
		for (ConstraintViolation<Artikel> v : violations) {
			sb.append(v.getMessage());
			sb.append(NEWLINE);
		}
		
		final String responseStr = sb.toString();
		final Response response = Response.status(CONFLICT)
		                                  .type(TEXT_PLAIN)
		                                  .entity(responseStr)
		                                  .build();
		return response;
	}

}

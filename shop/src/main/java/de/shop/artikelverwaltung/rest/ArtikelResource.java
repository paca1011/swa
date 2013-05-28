package de.shop.artikelverwaltung.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.lang.invoke.MethodHandles;
import java.net.URI;
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
import de.shop.util.LocaleHelper;
import de.shop.util.Log;
import de.shop.util.NotFoundException;
import de.shop.util.Transactional;

@Path("/artikel")
@Produces({ APPLICATION_XML, TEXT_XML, APPLICATION_JSON })
@Consumes
@RequestScoped
@Transactional
@Log


public class ArtikelResource {
	
private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	@Context
	private UriInfo uriInfo;

	@Inject
	private ArtikelService as;
	
	@PostConstruct
	private void postConstruct() {
		LOGGER.debugf("CDI-faehiges Bean %s wurde erzeugt", this);
	}
	
	@PreDestroy
	private void preDestroy() {
		LOGGER.debugf("CDI-faehiges Bean %s wird geloescht", this);
	} 
	
	@GET
	@Path("{id:[1-9][0-9]*}")
	public Artikel findArtikel(@PathParam("id") Long id) {
		final Artikel artikel = as.findArtikelById(id);
		if (artikel == null) {
			final String msg = "Kein Artikel gefunden mit der ID " + id;
			throw new NotFoundException(msg);
		}

		return artikel;
	}
	
	
	@Context
	private HttpHeaders headers;
	
	@Inject
	private UriHelperArtikel uriHelperArtikel;

	
	@Inject
	private LocaleHelper localeHelper;
	
	@GET
	@Produces(TEXT_PLAIN)
	@Path("version")
	public String getVersion() {
		return "1.0";
	}
	
	@GET
	@Path("{id:[1-9][0-9]*}")
	public Artikel findArtikelById(@PathParam("id") Long id, @Context UriInfo uriInfo) {
		final Artikel artikel = as.findArtikelById(id);
		if (artikel == null) {
			final String msg = "Kein Artikel gefunden mit der ID " + id;
			throw new NotFoundException(msg);
		}
		return artikel;
	}
	
	
	@POST
	@Consumes(APPLICATION_JSON)
	@Produces
	public Response createArtikel(Artikel artikel) {
		final Locale locale = localeHelper.getLocale(headers);
		artikel = as.createArtikel(artikel, locale);
		final URI artikelUri = uriHelperArtikel.getUriArtikel(artikel, uriInfo);
		
		return Response.created(artikelUri).build();
	}
	
	
	@PUT
	@Consumes(APPLICATION_JSON)
	@Produces
	public Response updateArtikel(Artikel artikel) {
		//@SuppressWarnings("unused")
		final Locale locale = localeHelper.getLocale(headers);
		
		as.updateArtikel(artikel, locale);
		return Response.noContent().build();
	}
	
}

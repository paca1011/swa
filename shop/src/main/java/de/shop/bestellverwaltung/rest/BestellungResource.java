package de.shop.bestellverwaltung.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.net.URI;
import java.util.Locale;

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

import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.bestellverwaltung.service.BestellungService;
import de.shop.kundenverwaltung.domain.Kunde;
import de.shop.kundenverwaltung.service.KundeService;
import de.shop.util.LocaleHelper;
import de.shop.util.NotFoundException;

@Path("/bestellungen")
@Produces(APPLICATION_JSON)
@Consumes
public class BestellungResource {
	@Context
	private UriInfo uriInfo;
	
	@Context
	private HttpHeaders headers;

	@Inject
	private UriHelperBestellung uriHelperBestellung;
	
	@Inject
	private LocaleHelper localeHelper;
	
	@Inject
	private BestellungService bs;
	
	@Inject
	private KundeService ks;
	
	@GET
	@Path("{id:[1-9][0-9]*}")
	public Bestellung findBestellungById(@PathParam("id") Long id) {
		final Bestellung bestellung = bs.findBestellungById(id);
		
		// TODO Anwendungskern statt Mock, Verwendung von Locale
		if (bestellung == null) {
			throw new NotFoundException("Keine Bestellung mit der ID " + id + " gefunden.");
		}
		
		// URLs innerhalb der gefundenen Bestellung anpassen
		uriHelperBestellung.updateUriBestellung(bestellung, uriInfo);
		return bestellung;
	}
	
	@POST
	@Consumes(APPLICATION_JSON)
	@Produces
	public Response createBestellung(Bestellung bestellung) {
		final Locale locale = localeHelper.getLocale(headers);
		
		// kundeId aus URI 
		URI kundeUri = bestellung.getKundeUri();
		String path = kundeUri.getPath();
		String idStr = path.substring(path.lastIndexOf('/') + 1);
		Long id = Long.parseLong(idStr);
				
		final Long kundeId = id;

		final Kunde kunde = ks.findKundeById(kundeId, locale);

		bestellung.setKunde(kunde);

		bestellung = bs.createBestellung(bestellung, kunde, locale);


		
		final URI bestellungUri = uriHelperBestellung.getUriBestellung(bestellung, uriInfo);
		return Response.created(bestellungUri).build();
	}
	
	@PUT
	@Consumes(APPLICATION_JSON)
	@Produces
	public Response updateBestellung(Bestellung bestellung) {
		final Locale locale = localeHelper.getLocale(headers);
		
		bestellung = bs.updateBestellung(bestellung, locale);
		return Response.noContent().build();
	}
}

package de.shop.artikelverwaltung.rest;

import static java.util.logging.Level.WARNING;

import java.net.URI;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.UriInfo;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.util.Log;

@ApplicationScoped
@Log
public class UriHelperArtikel {
	private static final Logger LOGGER = Logger.getLogger(UriHelperArtikel.class.getName());

	public URI getUriArtikel(Artikel artikel, UriInfo uriInfo) {
		if (artikel == null) {
			LOGGER.warning("Kein Artikel!!");
			return null;
		}
		if (artikel.getId() == null) {
			LOGGER.log(WARNING, "Noch keine ID des Artikels {0} gesetzt", artikel);
			return null;
		}
		
		final URI uriArtikel = uriInfo.getBaseUriBuilder()
				                      .path(ArtikelResource.class)
				                      .path(ArtikelResource.class, "findArtikelById")
				                      .build(artikel.getId());
		return uriArtikel;
	}
}

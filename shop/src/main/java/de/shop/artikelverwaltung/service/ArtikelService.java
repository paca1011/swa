package de.shop.artikelverwaltung.service;

import java.io.Serializable;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.util.Log;
import de.shop.util.Mock;

@Log
public class ArtikelService implements Serializable {
	private static final long serialVersionUID = -5105686816948437276L;

	public Artikel findArtikelById(Long id) {
		// TODO id pruefen
		// TODO Datenbanzugriffsschicht statt Mock
		return Mock.findArtikelById(id);
	}
}

package de.shop.artikelverwaltung.service;

import java.io.Serializable;
import java.util.Locale;

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
	
	public Artikel createArtikel(Artikel artikel, Locale locale) {
		if (artikel == null) {
			return artikel;
		}
		artikel = Mock.createArtikel(artikel);

		return artikel;
	}
	
	public Artikel updateArtikel(Artikel artikel, Locale locale) {
		if (artikel == null) {
			return null;
		}

		// Werden alle Constraints beim Modifizieren gewahrt?
		//validateArtikel(artikel, locale, Default.class, IdGroup.class);

		
		// TODO Datenbanzugriffsschicht statt Mock
		Mock.updateArtikel(artikel);
		
		return artikel;
	}
}

package de.shop.bestellverwaltung.service;

import java.io.Serializable;

import de.shop.bestellverwaltung.domain.Lieferant;
import de.shop.util.Log;
import de.shop.util.Mock;

@Log
public class LieferantService implements Serializable {
	private static final long serialVersionUID = -5105686816948437276L;

	public Lieferant findLieferantById(Long id) {
		// TODO id pruefen
		// TODO Datenbanzugriffsschicht statt Mock
		return Mock.findLieferantById(id);
	}
}

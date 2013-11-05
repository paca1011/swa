package de.shop.bestellverwaltung.service;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import de.shop.bestellverwaltung.domain.Lieferant;
import de.shop.util.interceptor.Log;

@Dependent
@Log
public class LieferantService implements Serializable {
	private static final long serialVersionUID = -5105686816948437276L;

	public Lieferant findLieferantById(Long id) {
		// TODO id pruefen
		// TODO Datenbanzugriffsschicht statt Mock
		return null;
	}
}

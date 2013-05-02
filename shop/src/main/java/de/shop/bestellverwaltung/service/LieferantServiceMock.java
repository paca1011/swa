package de.shop.bestellverwaltung.service;

import de.shop.bestellverwaltung.domain.Lieferant;
import de.shop.util.Log;
import de.shop.util.MockService;

@MockService
@Log
public class LieferantServiceMock extends LieferantService {
	private static final long serialVersionUID = -2919310633845009282L;

	@Override
	public Lieferant findLieferantById(Long id) {
		final Lieferant lieferant = new Lieferant();
		lieferant.setId(id);
		lieferant.setName("Name_" + id + "_Mock");
		return lieferant;
	}
}

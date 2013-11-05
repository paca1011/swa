package de.shop.bestellverwaltung.service;

import javax.enterprise.context.Dependent;

import de.shop.bestellverwaltung.domain.Lieferant;
import de.shop.util.cdi.MockService;
import de.shop.util.interceptor.Log;

@Dependent
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

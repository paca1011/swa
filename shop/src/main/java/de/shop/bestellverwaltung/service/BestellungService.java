package de.shop.bestellverwaltung.service;

import java.util.List;
import java.util.Locale;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.kundenverwaltung.domain.Kunde;

public interface BestellungService {
	/*
	 * 
	 */
	Bestellung findBestellungById(Long id);
	/*
	 * 
	 */
	Bestellung createBestellung(Bestellung bestellung, String email);
	/*
	 * 
	 */
	Bestellung createBestellung(Bestellung bestellung, Kunde kunde);
	/*
	 * 
	 */
	Bestellung updateBestellung(Bestellung bestellung, Locale locale);
	/*
	 * 
	 */
	Kunde findKundeById(Long id);
	/*
	 * 
	 */
	List<Bestellung> findBestellungenByKunde(Kunde kunde);
	/*
	 * 
	 */
	List<Artikel> ladenhueter(int anzahl);
}

package de.shop.util;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.bestellverwaltung.domain.Lieferant;
import de.shop.kundenverwaltung.domain.Kunde;
import de.shop.kundenverwaltung.domain.Adresse;


/**
 * Emulation des Anwendungskerns
 */
public final class Mock {
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	private static final int MAX_ID = 99;
	private static final int MAX_KUNDEN = 8;
	private static final int MAX_BESTELLUNGEN = 4;

	public static Kunde findKundeById(Long id) {
		if (id > MAX_ID) {
			return null;
		}
		
		final Kunde kunde = new Kunde();
		kunde.setId(id);
		kunde.setNachname("Nachname" + id);
		kunde.setEmail("" + id + "@hska.de");
		
		final Adresse adresse = new Adresse();
		adresse.setId(id + 1);        // andere ID fuer die Adresse
		adresse.setPlz("12345");
		adresse.setStadt("Teststadt");
		adresse.setHausnum("3a");
		adresse.setKunde(kunde);
		kunde.setAdresse(adresse);
		
		return kunde;
	}

	public static List<Kunde> findAllKunden() {
		final int anzahl = MAX_KUNDEN;
		final List<Kunde> kunden = new ArrayList<>(anzahl);
		for (int i = 1; i <= anzahl; i++) {
			final Kunde kunde = findKundeById(Long.valueOf(i));
			kunden.add(kunde);			
		}
		return kunden;
	}

	public static List<Kunde> findKundenByNachname(String nachname) {
		final int anzahl = nachname.length();
		final List<Kunde> kunden = new ArrayList<>(anzahl);
		for (int i = 1; i <= anzahl; i++) {
			final Kunde kunde = findKundeById(Long.valueOf(i));
			kunde.setNachname(nachname);
			kunden.add(kunde);			
		}
		return kunden;
	}
	
	public static Kunde findKundeByEmail(String email) {
		if (!email.startsWith("x")) {
			return null;
		}
		
		final Kunde kunde = new Kunde();
		kunde.setId(Long.valueOf(email.length()));
		kunde.setNachname("Nachname");
		kunde.setEmail(email);
		
		final Adresse adresse = new Adresse();
		adresse.setId(kunde.getId() + 1);        // andere ID fuer die Adresse
		adresse.setPlz("12345");
		adresse.setStadt("Teststadt");
		adresse.setKunde(kunde);
		kunde.setAdresse(adresse);
		
		return kunde;
	}

	public static List<Bestellung> findBestellungenByKundeId(Long kundeId) {
		final Kunde kunde = findKundeById(kundeId);
		
		// Beziehungsgeflecht zwischen Kunde und Bestellungen aufbauen
		final int anzahl = kundeId.intValue() % MAX_BESTELLUNGEN + 1;  // 1, 2, 3 oder 4 Bestellungen
		final List<Bestellung> bestellungen = new ArrayList<>(anzahl);
		for (int i = 1; i <= anzahl; i++) {
			final Bestellung bestellung = findBestellungById(Long.valueOf(i));
			bestellung.setKunde(kunde);
			bestellungen.add(bestellung);			
		}
		kunde.setBestellungen(bestellungen);
		
		return bestellungen;
	}

	public static Bestellung findBestellungById(Long id) {
		if (id > MAX_ID) {
			return null;
		}

		final Kunde kunde = findKundeById(id + 1); // andere ID fuer den Kunden
		
		final Bestellung bestellung = new Bestellung();
		bestellung.setId(id);
		bestellung.setStatus("laeuft");
		bestellung.setAusgeliefert(false);
		bestellung.setKunde(kunde);

		
		return bestellung;
	}

	public static Artikel findArtikelById(Long id) {
		if (id > MAX_ID) {
			return null;
		}
		
		final Artikel artikel = new Artikel();
		artikel.setId(id);
		artikel.setBezeichnung("Bezeichnung");
		artikel.setFarbe("Farbe");
		
		return artikel;
	}
	
	public static Artikel createArtikel(Artikel artikel) {
		final String bezeichnung = artikel.getBezeichnung();
		artikel.setId(Long.valueOf(bezeichnung.length()));
		
		System.out.println("Neuer Artikel: " + artikel);
		return artikel;
	}
	
	public static Bestellung createBestellung(Bestellung bestellung, Kunde kunde) {
		final String status = bestellung.getStatus();
		bestellung.setId(Long.valueOf(status.length()));
		bestellung.setKunde(kunde);
		kunde.getBestellungen().add(bestellung);

		LOGGER.infof("Neue Bestellung: %s fuer Kunde: %s", bestellung, kunde);
		return bestellung;
	}
	
//	public static Bestellung createBestellung(Bestellung bestellung, Kunde kunde) {
//		LOGGER.infof("Neue Bestellung: %s fuer Kunde: %s", bestellung, kunde);
//		return bestellung;
//	}
	
	
	public static Kunde createKunde(Kunde kunde) {
		// Neue IDs fuer Kunde und zugehoerige Adresse
		// Ein neuer Kunde hat auch keine Bestellungen
		final String nachname = kunde.getNachname();
		kunde.setId(Long.valueOf(nachname.length()));
		final Adresse adresse = kunde.getAdresse();
		adresse.setId((Long.valueOf(nachname.length())) + 1);
		adresse.setKunde(kunde);
		kunde.setBestellungen(null);
		
		System.out.println("Neuer Kunde: " + kunde);
		return kunde;
	}

	public static void updateKunde(Kunde kunde) {
		LOGGER.infof("Aktualisierter Kunde: %s", kunde);
	}

	public static void updateArtikel(Artikel artikel) {
		System.out.println("Aktualisierter Artikel: " + artikel);
	}
	
	public static void updateBestellung(Bestellung bestellung) {
		LOGGER.infof("Aktualisierter Kunde: %s" + bestellung);
	}
	
	public static void deleteKunde(Kunde kunde) {
		LOGGER.infof("Gelöschter Kunde: %s", kunde);
	}

	public static Lieferant findLieferantById(Long id) {
		final Lieferant lieferant = new Lieferant();
		lieferant.setId(id);
		lieferant.setName("Name_" + id);
		return lieferant;
	}
}

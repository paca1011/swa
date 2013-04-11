package de.shop.kundenverwaltung.domain;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import de.shop.bestellverwaltung.domain.Bestellung;


public class Kunde implements Serializable {
	private static final long serialVersionUID = 7401524595142572933L;
	
	private Long id;
	private String nachname;
	private String email;
	private String passwort;
	private Adresse adresse;
	@JsonIgnore
	private List<Bestellung> bestellungen;
	private URI bestellungenUri;
	private Date erstellt;
	private Date aktualisiert;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getAktualisiert() {
		return aktualisiert;
	}
	public void setAktualisiert(Date aktualisiert) {
		this.aktualisiert = aktualisiert;
	}
	public Date getErstellt() {
		return erstellt;
	}
	public void setErstellt(Date erstellt) {
		this.erstellt = erstellt;
	}
	public String getPasswort() {
		return passwort;
	}
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Adresse getAdresse() {
		return adresse;
	}
	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}
	public List<Bestellung> getBestellungen() {
		return bestellungen;
	}
	public void setBestellungen(List<Bestellung> bestellungen) {
		this.bestellungen = bestellungen;
	}
	public URI getBestellungenUri() {
		return bestellungenUri;
	}
	public void setBestellungenUri(URI bestellungenUri) {
		this.bestellungenUri = bestellungenUri;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Kunde other = (Kunde) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		}
		else if (!email.equals(other.email))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "AbstractKunde [id=" + id + ", nachname=" + nachname
				+ ", email=" + email + ", bestellungenUri=" + bestellungenUri + "]";
	}
}

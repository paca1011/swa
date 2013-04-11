package de.shop.bestellverwaltung.domain;

import java.util.Date;

import de.shop.kundenverwaltung.domain.Adresse;

public class Lieferant {

private Long id;
private String name;
private Long telefon_nr;
private Adresse adresse;
private Date erzeugt;
private Date aktualisiert;

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public Long getTelefon_nr() {
	return telefon_nr;
}

public void setTelefon_nr(Long telefon_nr) {
	this.telefon_nr = telefon_nr;
}

public Adresse getAdresse() {
	return adresse;
}

public void setAdresse(Adresse adresse) {
	this.adresse = adresse;
}

public Date getErzeugt() {
	return erzeugt;
}

public void setErzeugt(Date erzeugt) {
	this.erzeugt = erzeugt;
}

public Date getAktualisiert() {
	return aktualisiert;
}

public void setAktualisiert(Date aktualisiert) {
	this.aktualisiert = aktualisiert;
	
}

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((adresse == null) ? 0 : adresse.hashCode());
	result = prime * result
			+ ((aktualisiert == null) ? 0 : aktualisiert.hashCode());
	result = prime * result + ((erzeugt == null) ? 0 : erzeugt.hashCode());
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result
			+ ((telefon_nr == null) ? 0 : telefon_nr.hashCode());
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
	Lieferant other = (Lieferant) obj;
	if (adresse == null) {
		if (other.adresse != null)
			return false;
	} else if (!adresse.equals(other.adresse))
		return false;
	if (aktualisiert == null) {
		if (other.aktualisiert != null)
			return false;
	} else if (!aktualisiert.equals(other.aktualisiert))
		return false;
	if (erzeugt == null) {
		if (other.erzeugt != null)
			return false;
	} else if (!erzeugt.equals(other.erzeugt))
		return false;
	if (id == null) {
		if (other.id != null)
			return false;
	} else if (!id.equals(other.id))
		return false;
	if (name == null) {
		if (other.name != null)
			return false;
	} else if (!name.equals(other.name))
		return false;
	if (telefon_nr == null) {
		if (other.telefon_nr != null)
			return false;
	} else if (!telefon_nr.equals(other.telefon_nr))
		return false;
	return true;
}

@Override
public String toString() {
	return "Lieferant [id=" + id + ", name=" + name + ", telefon_nr="
			+ telefon_nr + ", adresse=" + adresse + ", erzeugt=" + erzeugt
			+ ", aktualisiert=" + aktualisiert + "]";
}
	
}

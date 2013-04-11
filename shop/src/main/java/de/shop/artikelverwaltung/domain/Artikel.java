package de.shop.artikelverwaltung.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Artikel {

private Long id;
private String bezeichnung;
private String farbe;
private BigDecimal preis_kunde;
private BigDecimal preis_lieferant;
private Long bestand;
private Date erzeugt;
private Date aktualisiert;

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getBezeichnung() {
	return bezeichnung;
}

public void setBezeichnung(String bezeichnung) {
	this.bezeichnung = bezeichnung;
}

public String getFarbe() {
	return farbe;
}

public void setFarbe(String farbe) {
	this.farbe = farbe;
}

public BigDecimal getPreis_kunde() {
	return preis_kunde;
}

public void setPreis_kunde(BigDecimal preis_kunde) {
	this.preis_kunde = preis_kunde;
}

public BigDecimal getPreis_lieferant() {
	return preis_lieferant;
}

public void setPreis_lieferant(BigDecimal preis_lieferant) {
	this.preis_lieferant = preis_lieferant;
}

public Long getBestand() {
	return bestand;
}

public void setBestand(Long bestand) {
	this.bestand = bestand;
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
	result = prime * result
			+ ((aktualisiert == null) ? 0 : aktualisiert.hashCode());
	result = prime * result + ((bestand == null) ? 0 : bestand.hashCode());
	result = prime * result
			+ ((bezeichnung == null) ? 0 : bezeichnung.hashCode());
	result = prime * result + ((erzeugt == null) ? 0 : erzeugt.hashCode());
	result = prime * result + ((farbe == null) ? 0 : farbe.hashCode());
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	result = prime * result
			+ ((preis_kunde == null) ? 0 : preis_kunde.hashCode());
	result = prime * result
			+ ((preis_lieferant == null) ? 0 : preis_lieferant.hashCode());
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
	Artikel other = (Artikel) obj;
	if (aktualisiert == null) {
		if (other.aktualisiert != null)
			return false;
	} else if (!aktualisiert.equals(other.aktualisiert))
		return false;
	if (bestand == null) {
		if (other.bestand != null)
			return false;
	} else if (!bestand.equals(other.bestand))
		return false;
	if (bezeichnung == null) {
		if (other.bezeichnung != null)
			return false;
	} else if (!bezeichnung.equals(other.bezeichnung))
		return false;
	if (erzeugt == null) {
		if (other.erzeugt != null)
			return false;
	} else if (!erzeugt.equals(other.erzeugt))
		return false;
	if (farbe == null) {
		if (other.farbe != null)
			return false;
	} else if (!farbe.equals(other.farbe))
		return false;
	if (id == null) {
		if (other.id != null)
			return false;
	} else if (!id.equals(other.id))
		return false;
	if (preis_kunde == null) {
		if (other.preis_kunde != null)
			return false;
	} else if (!preis_kunde.equals(other.preis_kunde))
		return false;
	if (preis_lieferant == null) {
		if (other.preis_lieferant != null)
			return false;
	} else if (!preis_lieferant.equals(other.preis_lieferant))
		return false;
	return true;
}

@Override
public String toString() {
	return "Artikel [id=" + id + ", bezeichnung=" + bezeichnung + ", farbe="
			+ farbe + ", preis_kunde=" + preis_kunde + ", preis_lieferant="
			+ preis_lieferant + ", bestand=" + bestand + ", erzeugt=" + erzeugt
			+ ", aktualisiert=" + aktualisiert + "]";
}

}

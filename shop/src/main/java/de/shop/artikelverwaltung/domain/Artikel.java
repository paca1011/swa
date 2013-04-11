package de.shop.artikelverwaltung.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Artikel implements Serializable {
	private static final long serialVersionUID = 6240743788335631652L;
	
	private Long id;
	private String bezeichnung;
	private String farbe;
	private BigDecimal preisKunde;
	private BigDecimal preisLieferant;
	private Long bestand;
	// TODO kommt hier jeweils @Jsonignore?
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
	public BigDecimal getPreisKunde() {
		return preisKunde;
	}
	public void setPreisKunde(BigDecimal preisKunde) {
		this.preisKunde = preisKunde;
	}
	public BigDecimal getPreisLieferant() {
		return preisLieferant;
	}
	public void setPreisLieferant(BigDecimal preisLieferant) {
		this.preisLieferant = preisLieferant;
	}
	public Long getBestand() {
		return bestand;
	}
	public void setBestand(Long bestand) {
		this.bestand = bestand;
	}
	//TODO hier dann @JsonProperty("datum") ??
	public Date getErzeugt() {
		return erzeugt == null ? null : (Date) erzeugt.clone();
	}
	public void setErzeugt(Date erzeugt) {
		this.erzeugt = erzeugt == null ? null : (Date) erzeugt.clone();
	}
	public Date getAktualisiert() {
		return aktualisiert == null ? null : (Date) erzeugt.clone();
	}
	public void setAktualisiert(Date aktualisiert) {
		this.aktualisiert = aktualisiert == null ? null : (Date) aktualisiert.clone();
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
				+ ((preisKunde == null) ? 0 : preisKunde.hashCode());
		result = prime * result
				+ ((preisLieferant == null) ? 0 : preisLieferant.hashCode());
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
		final Artikel other = (Artikel) obj;
		if (aktualisiert == null) {
			if (other.aktualisiert != null)
				return false;
		}
		else if (!aktualisiert.equals(other.aktualisiert))
			return false;
		if (bestand == null) {
			if (other.bestand != null)
				return false;
		} 
		else if (!bestand.equals(other.bestand))
			return false;
		if (bezeichnung == null) {
			if (other.bezeichnung != null)
				return false;
		} 
		else if (!bezeichnung.equals(other.bezeichnung))
			return false;
		if (erzeugt == null) {
			if (other.erzeugt != null)
				return false;
		} 
		else if (!erzeugt.equals(other.erzeugt))
			return false;
		if (farbe == null) {
			if (other.farbe != null)
				return false;
		} 
		else if (!farbe.equals(other.farbe))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} 
		else if (!id.equals(other.id))
			return false;
		if (preisKunde == null) {
			if (other.preisKunde != null)
				return false;
		} 
		else if (!preisKunde.equals(other.preisKunde))
			return false;
		if (preisLieferant == null) {
			if (other.preisLieferant != null)
				return false;
		} 
		else if (!preisLieferant.equals(other.preisLieferant))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Artikel [id=" + id + ", bezeichnung=" + bezeichnung + ", farbe="
				+ farbe + ", preis_kunde=" + preisKunde + ", preis_lieferant="
				+ preisLieferant + ", bestand=" + bestand + ", erzeugt=" + erzeugt
				+ ", aktualisiert=" + aktualisiert + "]";
	}
	
	}

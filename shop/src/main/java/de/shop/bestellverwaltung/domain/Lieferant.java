package de.shop.bestellverwaltung.domain;

import java.io.Serializable;
import java.util.Date;

import de.shop.kundenverwaltung.domain.Adresse;

public class Lieferant implements Serializable {
	private static final long serialVersionUID = -1890561212075707472L;
	
	private Long id;
	private String name;
	private Long telefonnum;
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
	public Long getTelefonnum() {
		return telefonnum;
	}
	public void setTelefonnum(Long telefonnum) {
		this.telefonnum = telefonnum;
	}
	public Adresse getAdresse() {
		return adresse;
	}
	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}
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
		result = prime * result + ((adresse == null) ? 0 : adresse.hashCode());
		result = prime * result
				+ ((aktualisiert == null) ? 0 : aktualisiert.hashCode());
		result = prime * result + ((erzeugt == null) ? 0 : erzeugt.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((telefonnum == null) ? 0 : telefonnum.hashCode());
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
		final Lieferant other = (Lieferant) obj;
		if (adresse == null) {
			if (other.adresse != null)
				return false;
		} 
		else if (!adresse.equals(other.adresse))
			return false;
		if (aktualisiert == null) {
			if (other.aktualisiert != null)
				return false;
		} 
		else if (!aktualisiert.equals(other.aktualisiert))
			return false;
		if (erzeugt == null) {
			if (other.erzeugt != null)
				return false;
		} 
		else if (!erzeugt.equals(other.erzeugt))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} 
		else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} 
		else if (!name.equals(other.name))
			return false;
		if (telefonnum == null) {
			if (other.telefonnum != null)
				return false;
		} 
		else if (!telefonnum.equals(other.telefonnum))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Lieferant [id=" + id + ", name=" + name + ", telefon_nr="
				+ telefonnum + ", adresse=" + adresse + ", erzeugt=" + erzeugt
				+ ", aktualisiert=" + aktualisiert + "]";
	}
		
	}

package de.shop.kundenverwaltung.domain;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Adresse implements Serializable {
	private static final long serialVersionUID = -3029272617931844501L;
	private Long id;
	private String plz;
	private String stadt;
	private String haus_nr;
	
	@JsonIgnore
	private Kunde kunde;
	
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
	public String getHaus_nr() {
		return haus_nr;
	}
	public void setHaus_nr(String haus_nr) {
		this.haus_nr = haus_nr;
	}
	public String getPlz() {
		return plz;
	}
	public void setPlz(String plz) {
		this.plz = plz;
	}
	public String getStadt() {
		return stadt;
	}
	public void setStadt(String stadt) {
		this.stadt = stadt;
	}
	
	public Kunde getKunde() {
		return kunde;
	}
	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((stadt == null) ? 0 : stadt.hashCode());
		result = prime * result + ((plz == null) ? 0 : plz.hashCode());
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
		Adresse other = (Adresse) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (stadt == null) {
			if (other.stadt != null)
				return false;
		}
		else if (!stadt.equals(other.stadt))
			return false;
		if (plz == null) {
			if (other.plz != null)
				return false;
		}
		else if (!plz.equals(other.plz))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Adresse [id=" + id + ", plz=" + plz + ", ort=" + stadt + "]";
	}
}

package de.shop.kundenverwaltung.domain;

import static de.shop.util.Constants.MIN_ID;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import de.shop.bestellverwaltung.domain.Lieferant;
import de.shop.util.IdGroup;

@Entity
@Table(name = "adresse")
public class Adresse implements Serializable {
	private static final long serialVersionUID = -3029272617931844501L;
	
	public static final int STADT_LENGTH_MAX = 128;
	public static final int STADT_LENGTH_MIN = 3;
	
	@Id
	@GeneratedValue
	@Column(nullable = false, updatable = false)
	@Min(value = MIN_ID, message = "{kundenverwaltung.adresse.id.min}", groups = IdGroup.class)
	private Long id;
	
	private String plz;
	
//	@NotNull(message = "{kundenverwaltung.adresse.stadt.notNull}")
	@Size(max = STADT_LENGTH_MAX, min = STADT_LENGTH_MIN, message = "{kundenverwaltung.adresse.stadt.length}")
	private String stadt;
	
	private String strasse;
	
	private String hausnum;
	
	// TODO Kunde muss jetzt null sein duerfen
	@OneToOne
	@JoinColumn(name = "kunde_fk", nullable = true)
//	@NotNull(message = "{kundenverwaltung.adresse.kunde.notNull}")
	@XmlTransient
	private Kunde kunde;
	
	@OneToOne
	@JoinColumn(name = "lieferant_fk", nullable = true)
	@XmlTransient
	private Lieferant lieferant;

	private Date erzeugt;
	
	private Date aktualisiert;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStrasse() {
		return strasse;
	}
	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}
	public String getHausnum() {
		return hausnum;
	}
	public void setHausnum(String hausnum) {
		this.hausnum = hausnum;
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
	public Lieferant getLieferant() {
		return lieferant;
	}
	public void setLieferant(Lieferant lieferant) {
		this.lieferant = lieferant;
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
		final Adresse other = (Adresse) obj;
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
		return "Adresse [id=" + id + ", plz=" + plz + ", stadt=" + stadt 
				+ ", strasse=" + strasse + ", hausnum =" + hausnum + "]";
	}
}

package de.shop.bestellverwaltung.domain;

import static de.shop.util.Constants.ERSTE_VERSION;
import static de.shop.util.Constants.MIN_ID;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import de.shop.kundenverwaltung.domain.Adresse;
import de.shop.util.IdGroup;

@Entity
@Table(name = "lieferant")
@XmlRootElement
public class Lieferant implements Serializable {
	private static final long serialVersionUID = -1890561212075707472L;


	private static final int NAME_LENGTH_MIN = 2;
	private static final int NAME_LENGTH_MAX = 32;
	
	
	@Id
	@GeneratedValue
	@Min(value = MIN_ID, message = "{bestellverwaltung.lieferant.id.min}", groups = IdGroup.class)
	@Column(nullable = false, updatable = false)
	private Long id;
	
	@Version
	@Basic(optional = false)
	private int version = ERSTE_VERSION;
	
	@NotNull(message = "{beststellverwaltung.lieferung.name.notNull}")
	@Size(min = NAME_LENGTH_MIN, max = NAME_LENGTH_MAX,
	      message = "{bestellverwaltung.lieferant.name.length}")
	private String name;
	
	@Column(name = "telefonnum", nullable = false)
	private Long telefonnum;
	
	
	@OneToOne(cascade = { PERSIST, REMOVE }, mappedBy = "lieferant")
	@Valid
	@NotNull(message = "{kundenverwaltung.kunde.adresse.notNull}")
	private Adresse adresse;
	
	@OneToMany
	@JoinColumn(name = "lieferant_fk")
	@XmlTransient
	private List<Bestellung> bestellung;
	
	@Column(nullable = false)
	@Temporal(TIMESTAMP)
	@XmlTransient
	private Date erzeugt;
	
	@Column(nullable = false)
	@Temporal(TIMESTAMP)
	@XmlTransient
	private Date aktualisiert;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Bestellung> getBestellung() {
		return bestellung;
	}
	public void setBestellung(List<Bestellung> bestellung) {
		this.bestellung = bestellung;
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

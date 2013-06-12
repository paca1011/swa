package de.shop.artikelverwaltung.domain;

import static de.shop.util.Constants.MIN_ID;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.jboss.logging.Logger;

import de.shop.util.IdGroup;

@Entity
@Table(name = "artikel")
@NamedQueries({
	@NamedQuery(name  = Artikel.FIND_VERFUEGBARE_ARTIKEL,
        	query = "SELECT      a"
        	        + " FROM     Artikel a"
					+ " WHERE    a.bestand > 0"
                    + " ORDER BY a.id ASC"),
	@NamedQuery(name  = Artikel.FIND_ARTIKEL_BY_BEZ,
            	query = "SELECT      a"
                        + " FROM     Artikel a"
						+ " WHERE    a.bezeichnung LIKE :" + Artikel.PARAM_BEZEICHNUNG
						+ "          AND a.bestand > 0"
			 	        + " ORDER BY a.id ASC"),
   	@NamedQuery(name  = Artikel.FIND_ARTIKEL_MAX_PREIS,
            	query = "SELECT      a"
                        + " FROM     Artikel a"
						+ " WHERE    a.preisKunde < :" + Artikel.PARAM_PREIS
			 	        + " ORDER BY a.id ASC")
})

public class Artikel implements Serializable {
	private static final long serialVersionUID = 6240743788335631652L;
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public static final int BEZEICHNUNG_LENGTH_MIN = 3;
	public static final int BEZEICHNUNG_LENGTH_MAX = 33;
	
	public static final int FARBE_LENGTH_MIN = 3;
	public static final int FARBE_LENGTH_MAX = 32;
	
	private static final String PREFIX = "Artikel.";
	public static final String FIND_VERFUEGBARE_ARTIKEL = PREFIX + "findVerfuegbareArtikel";
	public static final String FIND_ARTIKEL_BY_BEZ = PREFIX + "findArtikelByBez";
	public static final String FIND_ARTIKEL_MAX_PREIS = PREFIX + "findArtikelByMaxPreis";

	public static final String PARAM_BEZEICHNUNG = "bezeichnung";
	public static final String PARAM_PREIS = "preisKunde";
	
	@Id
	@GeneratedValue
	@Column(nullable = false, updatable = false)
	@Min(value = MIN_ID, message = "{artikelverwaltung.artikel.id.min}", groups = IdGroup.class)
	private Long id;
	
	@Column(length = BEZEICHNUNG_LENGTH_MAX, nullable = false)
	@NotNull(message = "{artikelverwaltung.artikel.bezeichnung.notNull}")
	@Size(min = BEZEICHNUNG_LENGTH_MIN, max = BEZEICHNUNG_LENGTH_MAX,
    message = "{artikelverwaltung.artikel.bezeichnung.length}")
	private String bezeichnung;
	
	@Column(length = FARBE_LENGTH_MAX)
	@Size(min = FARBE_LENGTH_MIN, max = FARBE_LENGTH_MAX,
	message = "{artikelverwaltung.artikel.farbe.length}")
	private String farbe;
	
	@Column(nullable = false)
	@NotNull(message = "{artikelverwaltung.artikel.preiskunde.notNull}")
	@Min(value = 0, message = "{artikelverwaltung.artikel.preiskunde.min}")
	private BigDecimal preisKunde;
	
	@Column(nullable = false)
	@NotNull(message = "{artikelverwaltung.artikel.preislieferant.notNull}")
	@Min(value = 0, message = "{artikelverwaltung.artikel.preislieferant.min}")
	private BigDecimal preisLieferant;
	
	@Column(nullable = false)
	@NotNull(message = "{artikelverwaltung.artikel.bestand.notNull}")
	@Min(value = 1, message = "{artikelverwaltung.artikel.bestand.min}")
	private Long bestand;
	
	@Column(nullable = false)
	@Temporal(TIMESTAMP)
	@JsonIgnore
	private Date erzeugt;
	
	@Column(nullable = false)
	@Temporal(TIMESTAMP)
	@JsonIgnore
	private Date aktualisiert;
	
	public Artikel() {
		super();
	}
	
	public Artikel(String bezeichnung, BigDecimal preisKunde, BigDecimal preisLieferant) {
		super();
		this.bezeichnung = bezeichnung;
		this.preisKunde = preisKunde;
		this.preisLieferant = preisLieferant;
	}

	@PrePersist
	private void prePersist() {
		erzeugt = new Date();
		aktualisiert = new Date();
	}
	
	@PostPersist
	private void postPersist() {
		LOGGER.debugf("Neuer Artikel mit ID=%d", id);
	}
	
	@PreUpdate
	private void preUpdate() {
		aktualisiert = new Date();
	}
	
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

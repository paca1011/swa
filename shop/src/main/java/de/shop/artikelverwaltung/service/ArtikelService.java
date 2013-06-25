package de.shop.artikelverwaltung.service;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.jboss.logging.Logger;

import com.google.common.base.Strings;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.util.IdGroup;
import de.shop.util.Log;
import de.shop.util.ValidatorProvider;

@Log
public class ArtikelService implements Serializable {
	private static final long serialVersionUID = -5105686816948437276L;
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	@PersistenceContext
	private transient EntityManager em;
	
	@PostConstruct
	private void postConstruct() {
		LOGGER.debugf("CDI-faehiges Bean %s wurde erzeugt", this);
	}
	
	@PreDestroy
	private void preDestroy() {
		LOGGER.debugf("CDI-faehiges Bean %s wird geloescht", this);
	}
	
	@Inject
	private ValidatorProvider validatorProvider;

	private void validateArtikel(Artikel artikel, Locale locale, Class<?>... groups) {
		
		final Validator validator = validatorProvider.getValidator(locale);
		
		final Set<ConstraintViolation<Artikel>> violations = validator.validate(artikel, groups);
		if (!violations.isEmpty()) {
			throw new InvalidArtikelException(artikel, violations);
		}
	}
	
	public List<Artikel> findArtikelByBezeichnung(String bezeichnung, Locale locale) {
		validateBezeichnung(bezeichnung, locale);
		
		if (Strings.isNullOrEmpty(bezeichnung)) {
			final List<Artikel> vieleartikel = findVerfuegbareArtikel();
			return vieleartikel;
		}
		final List<Artikel> vieleartikel = em.createNamedQuery(Artikel.FIND_ARTIKEL_BY_BEZ, Artikel.class)
                .setParameter(Artikel.PARAM_BEZEICHNUNG, "%" + bezeichnung + "%")
                .getResultList();
		return vieleartikel;
	}
	
	private void validateBezeichnung(String bezeichnung, Locale locale) {
		final Validator validator = validatorProvider.getValidator(locale);
		final Set<ConstraintViolation<Artikel>> violations = validator.validateValue(Artikel.class,
				                                                                           "bezeichnung",
				                                                                           bezeichnung,
				                                                                           Default.class);
		if (!violations.isEmpty())
			throw new InvalidBezeichnungException(bezeichnung, violations);
	}
	
	public List<Artikel> findVerfuegbareArtikel() {
		final List<Artikel> result = em.createNamedQuery(Artikel.FIND_VERFUEGBARE_ARTIKEL, Artikel.class)
				                       .getResultList();
		return result;
	}
	
	public Artikel findArtikelById(Long id) {
		final Artikel artikel = em.find(Artikel.class, id);
		return artikel;
	}
	
	public List<Artikel> findArtikelByIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyList();
		}
		
		/**
		 * SELECT a
		 * FROM   Artikel a
		 * WHERE  a.id = ? OR a.id = ? OR ...
		 */
		final CriteriaBuilder builder = em.getCriteriaBuilder();
		final CriteriaQuery<Artikel> criteriaQuery = builder.createQuery(Artikel.class);
		final Root<Artikel> a = criteriaQuery.from(Artikel.class);

		final Path<Long> idPath = a.get("id");
		
		
		Predicate pred = null;
		if (ids.size() == 1) {
			// Ohne OR da nur eine ID
			pred = builder.equal(idPath, ids.get(0));
		}
		else {
			// Mind. 2x id, durch OR verknuepft
			final Predicate[] equals = new Predicate[ids.size()];
			int i = 0;
			for (Long id : ids) {
				equals[i++] = builder.equal(idPath, id);
			}
			
			pred = builder.or(equals);
		}
		
		criteriaQuery.where(pred);
		
		final TypedQuery<Artikel> query = em.createQuery(criteriaQuery);

		final List<Artikel> artikel = query.getResultList();
		return artikel;
	}
	
	public Artikel createArtikel(Artikel artikel, Locale locale) {
		if (artikel == null) {
			return artikel;
		}
		validateArtikel(artikel, locale, Default.class);
		
		em.persist(artikel);
		return artikel;
	}
	
	public Artikel updateArtikel(Artikel artikel, Locale locale) {
		if (artikel == null) {
			return null;
		}
		
		validateArtikel(artikel, locale, Default.class, IdGroup.class);

		
		em.merge(artikel);
		
		return artikel;
	}
}

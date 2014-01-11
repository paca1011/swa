package de.shop.artikelverwaltung.web;

import de.shop.auth.web.AuthModel;
import static de.shop.util.Constants.JSF_INDEX;
import static de.shop.util.Constants.JSF_REDIRECT_SUFFIX;
import static javax.ejb.TransactionAttributeType.SUPPORTS;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.faces.context.Flash;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.richfaces.push.cdi.Push;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.artikelverwaltung.service.ArtikelService;
import de.shop.kundenverwaltung.service.EmailExistsException;
import de.shop.util.AbstractShopException;
import de.shop.util.interceptor.Log;
import de.shop.util.persistence.ConcurrentDeletedException;
import de.shop.util.web.Captcha;
import de.shop.util.web.Client;
import de.shop.util.web.Messages;


@Named
@SessionScoped
@Stateful
@TransactionAttribute(SUPPORTS)
public class ArtikelModel implements Serializable {
	private static final long serialVersionUID = 1564024850446471639L;

	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	private static final String JSF_LIST_ARTIKEL = "/artikelverwaltung/listArtikel";
	private static final String FLASH_ARTIKEL = "artikel";
	private static final String JSF_SELECT_ARTIKEL = "/artikelverwaltung/selectArtikel";
	private static final String SESSION_VERFUEGBARE_ARTIKEL = "verfuegbareArtikel";
	
	private static final String CLIENT_ID_CREATE_CAPTCHA_INPUT = "createArtikelForm:captchaInput";
	private static final String MSG_KEY_CREATE_ARTIKEL_WRONG_CAPTCHA = "artikel.wrongCaptcha";

	private String bezeichnung;

	@Inject
	private ArtikelService as;
	
	@Inject
	private Flash flash;
	
	@Inject
	private AuthModel auth;
	
	@Inject
	@Client
	private Locale locale;
	
	@Inject
	private Messages messages;
	
	@Inject
	@Push(topic = "marketing")
	private transient Event<String> neuerArtikelEvent;
	
	@Inject
	@Push(topic = "updateArtikel")
	private transient Event<String> updateArtikelEvent;
	
	@Inject
	private Captcha captcha;
	
	@Inject
	private transient HttpSession session;
	
	private Artikel artikel;
	
	private String captchaInput;

	private boolean geaendertArtikel;    // fuer ValueChangeListener
	
	@PostConstruct
	private void postConstruct() {
		LOGGER.debugf("CDI-faehiges Bean %s wurde erzeugt", this);
	}

	@PreDestroy
	private void preDestroy() {
		LOGGER.debugf("CDI-faehiges Bean %s wird geloescht", this);
	}
	
	public Artikel getArtikel() {
		return artikel;
	}

	public void setArtikel(Artikel artikel) {
		this.artikel = artikel;
	}

	@Override
	public String toString() {
		return "ArtikelModel [bezeichnung=" + bezeichnung + "]";
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	
	public String getCaptchaInput() {
		return captchaInput;
	}

	public void setCaptchaInput(String captchaInput) {
		this.captchaInput = captchaInput;
	}

	@Log
	public String findArtikelByBezeichnung() {
		final List<Artikel> artikel = as.findArtikelByBezeichnung(bezeichnung);
		flash.put(FLASH_ARTIKEL, artikel);

		return JSF_LIST_ARTIKEL;
	}
	
	@Log
	public String selectArtikel() {
		if (session.getAttribute(SESSION_VERFUEGBARE_ARTIKEL) == null) {
			final List<Artikel> alleArtikel = as.findVerfuegbareArtikel();
			session.setAttribute(SESSION_VERFUEGBARE_ARTIKEL, alleArtikel);
		}
		
		return JSF_SELECT_ARTIKEL;
	}
	
	
	@TransactionAttribute
	@Log
	public String createArtikel() {
		if (!captcha.getValue().equals(captchaInput)) {
			final String outcome = createArtikelErrorMsg(null);
			return outcome;
		}

		try {
			artikel = as.createArtikel(artikel);
		}
		catch (EmailExistsException e) {
			return createArtikelErrorMsg(e);
		}
		
		// Push-Event fuer Webbrowser
		neuerArtikelEvent.fire(String.valueOf(artikel.getId()));
		
		// Aufbereitung fuer viewKunde.xhtml
//		kundeId = neuerKunde.getId();
//		kunde = neuerKunde;
		artikel = null;
//		neuerKunde = null;  // zuruecksetzen
		
		return "/index";
	}
	
	private String createArtikelErrorMsg(AbstractShopException e) {
		if (e == null) {
			messages.error(MSG_KEY_CREATE_ARTIKEL_WRONG_CAPTCHA, locale, CLIENT_ID_CREATE_CAPTCHA_INPUT);
		}
		
		
		return null;
	}

	public void createEmptyArtikel() {
		captchaInput = null;

		if (artikel != null) {
			return;
		}

		artikel = new Artikel();
		
	}
	
	public void geaendert(ValueChangeEvent e) {
		if (geaendertArtikel) {
			return;
		}
		
		if (e.getOldValue() == null) {
			if (e.getNewValue() != null) {
				geaendertArtikel = true;
			}
			return;
		}

		if (!e.getOldValue().equals(e.getNewValue())) {
			geaendertArtikel= true;				
		}
	}
	
	@TransactionAttribute
	@Log
	public String update() {
		auth.preserveLogin();
		
		if (!geaendertArtikel || artikel == null) {
			return JSF_INDEX;
		}
		
		LOGGER.tracef("Aktualisierter Kunde: %s", artikel);
		try {
			artikel = as.updateArtikel(artikel);
		}
		catch (EmailExistsException | ConcurrentDeletedException | OptimisticLockException e) {
//			final String outcome = updateErrorMsg(e, artikel.getClass());
//			return outcome;
		}

		// Push-Event fuer Webbrowser
		updateArtikelEvent.fire(String.valueOf(artikel.getId()));
		return JSF_INDEX + JSF_REDIRECT_SUFFIX;
	}
	
//	private String updateErrorMsg(RuntimeException e, Class<? extends Kunde> kundeClass) {
//		final Class<? extends RuntimeException> exceptionClass = e.getClass();
//		if (EmailExistsException.class.equals(exceptionClass)) {
//			final EmailExistsException e2 = EmailExistsException.class.cast(e);
//			messages.error(MSG_KEY_EMAIL_EXISTS, locale, CLIENT_ID_UPDATE_EMAIL, e2.getEmail());
//		}
//		else if (OptimisticLockException.class.equals(exceptionClass)) {
//			messages.error(MSG_KEY_CONCURRENT_UPDATE, locale, null);
//
//		}
//		else if (ConcurrentDeletedException.class.equals(exceptionClass)) {
//			messages.error(MSG_KEY_CONCURRENT_DELETE, locale, null);
//		}
//		else {
//			throw new RuntimeException(e);
//		}
//		return null;
//	}
	
//	@Log
//	public String selectForUpdate(Kunde ausgewaehlterKunde) {
//		if (ausgewaehlterKunde == null) {
//			return null;
//		}
//		
//		kunde = ausgewaehlterKunde;
//		
//		return Kunde.class.equals(ausgewaehlterKunde.getClass())
//			   ? JSF_UPDATE_PRIVATKUNDE
//			   : JSF_UPDATE_FIRMENKUNDE;
//	}
//	
}

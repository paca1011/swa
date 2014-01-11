package de.shop.artikelverwaltung.web;

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
import javax.enterprise.inject.Model;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.richfaces.push.cdi.Push;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.artikelverwaltung.service.ArtikelService;
import de.shop.kundenverwaltung.service.EmailExistsException;
import de.shop.util.AbstractShopException;
import de.shop.util.interceptor.Log;
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
	
	private static final String JSF_ARTIKELVERWALTUNG = "/artikelverwaltung/";
	private static final String JSF_VIEW_ARTIKEL = JSF_ARTIKELVERWALTUNG + "viewArtikel";
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
	@Client
	private Locale locale;
	
	@Inject
	private Messages messages;
	
	@Inject
	@Push(topic = "marketing")
	private transient Event<String> neuerArtikelEvent;
	
	@Inject
	private Captcha captcha;
	
	@Inject
	private transient HttpSession session;
	
	private Artikel artikel;
	
	private String captchaInput;

	
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
}

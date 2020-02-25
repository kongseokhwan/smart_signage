package com.kulcloud.signage.tenant.ui;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.vaadin.flow.i18n.I18NProvider;

@Component
public class TranslationProvider implements I18NProvider {

	private static final long serialVersionUID = 1L;

	@Override
	public List<Locale> getProvidedLocales() {
		return Collections.emptyList();
	}

	@Override
	public String getTranslation(String key, Locale locale, Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

}

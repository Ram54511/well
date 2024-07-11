package com.dcode7.iwell.common.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LocalService {

	@Autowired
	private MessageSource messageSource;

	public String getMessage(String code) {
		Locale locale = LocaleContextHolder.getLocale();
		System.out.println(locale);
		return this.messageSource.getMessage(code, null, locale);
	}

	public String getMessage(String code, Object... args) {
		Locale locale = LocaleContextHolder.getLocale();
		return this.messageSource.getMessage(code, args, locale);
	}

	public String getMessageWithLang(String code, String lang) {
		Locale locale = new Locale(lang);
		return this.messageSource.getMessage(code, null, locale);
	}

	public String getMessageWithLangAndArgs(String lang, String code, Object... args) {
		Locale locale = new Locale(lang);
		return this.messageSource.getMessage(code, args, locale);
	}
}
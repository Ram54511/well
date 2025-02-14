//package com.dcode7.iwell.utils;
//
//import java.util.Locale;
//
//import org.springframework.context.MessageSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.support.ReloadableResourceBundleMessageSource;
//import org.springframework.web.servlet.LocaleResolver;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
//import org.springframework.web.servlet.i18n.SessionLocaleResolver;
//
//@Configuration
//public class LocaleConfig implements WebMvcConfigurer {
//
//    @Bean
//    MessageSource messageSource() {
//		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//		messageSource.setBasenames("classpath:locale/messages");
//		messageSource.setUseCodeAsDefaultMessage(true);
//		messageSource.setDefaultEncoding("UTF-8");
//		messageSource.setCacheSeconds(3600);
//		return messageSource;
//	}
//
//    @Bean
//    LocaleResolver localeResolver() {
//		SessionLocaleResolver slr = new SessionLocaleResolver();
//		slr.setDefaultLocale(Locale.US);
//		return slr;
//	}
//
//    @Bean
//    LocaleChangeInterceptor localeChangeInterceptor() {
//		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
//		lci.setParamName("lang");
//		return lci;
//	}
//
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(localeChangeInterceptor());
//	}
//}
package com.dcode7.iwell.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RestTemplateHandler {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateHandler.class);

	/** The rest template. */
	private static RestTemplate restTemplate = new RestTemplate();

	/**
	 * Used for accessing third party api without header and body.
	 *
	 * @param <T>          the generic type
	 * @param uriBuilder   the uri builder
	 * @param httpMethod   the http method
	 * @param responseType the response type
	 * @return ResponseEntity of same type which class sent with request
	 */
	public static <T> ResponseEntity<T> exchange(UriComponentsBuilder uriBuilder, HttpMethod httpMethod,
			Class<T> responseType) {
		ResponseEntity<T> response = null;
		try {
			response = restTemplate.exchange(uriBuilder.build().toUri(), httpMethod, new HttpEntity<String>() {
			}, responseType);
		} catch (HttpClientErrorException e) {
			LOGGER.error("Error in hitting url : {} response is: {}", uriBuilder.build().toUri().toString(),
					e.getResponseBodyAsString());
		}
		return response;
	}

}

package com.dcode7.iwell.security.filter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.dcode7.iwell.constant.URLConstant;
import com.dcode7.iwell.security.factory.JWTTokenFactory;
import com.dcode7.iwell.user.service.UserService;

@Configuration
@EnableWebSecurity
@Order(1)
@EnableMethodSecurity
public class WebSecurityConfig {
	@Value("${jwt.secret}")
	private String tokenSecret;

	@Value("${jwt.header}")
	private String header;

	@Value("${jwt.prefix}")
	private String prefix;

	@Autowired
	private UserService userService;

	@Autowired
	private JWTTokenFactory jwtTokenFactory;

	private static final String[] PUBLIC_URLS = { "/swagger-ui/**", "/api-docs/swagger-config", "/api-docs",
			URLConstant.REGISTER,URLConstant.FIELDAGENT_REGISTER, URLConstant.LOGIN, URLConstant.REFERRAL_DETAILS, URLConstant.REGISTRATION_TOKEN };

	private void configurePermitAllEndpoints(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(requests -> requests.requestMatchers(PUBLIC_URLS).permitAll()
				.requestMatchers(HttpMethod.POST, "/users/register","/users/fieldagentregister", "/users/login").permitAll().anyRequest()
				.authenticated());
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

		httpSecurity

				.addFilterAfter(new JWTAuthorizationFilter(jwtTokenFactory, userService),
						UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(mgmt -> mgmt.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(AbstractHttpConfigurer::disable);

		configurePermitAllEndpoints(httpSecurity);
		return httpSecurity.build();

	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
		configuration.setExposedHeaders(List.of("Authorization"));

		UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
		configurationSource.registerCorsConfiguration("/**", configuration);

		return configurationSource;

	}
}
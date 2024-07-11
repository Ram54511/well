package com.dcode7.iwell.security.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dcode7.iwell.security.factory.JWTTokenFactory;
import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

	private UserService userService;

	private JWTTokenFactory jwtTokenFactory;

	public JWTAuthorizationFilter(JWTTokenFactory jwtTokenFactory, UserService userService) {

		this.jwtTokenFactory = jwtTokenFactory;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(req);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		if (token != null) {
			token = token.replace("Bearer", "").trim();

			String subject = null;
			subject = jwtTokenFactory.getSubjectFromJwtToken(token);

			if (subject != null) {
				Optional<User> optionalUser = userService.findByEmail(subject);
				if (optionalUser.isPresent()) {
					User user = optionalUser.get();
					System.out.println(user);
					return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				}
			}
		}
		return null;
	}
}
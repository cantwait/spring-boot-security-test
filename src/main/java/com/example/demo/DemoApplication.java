package com.example.demo;


import com.example.demo.model.Role;
import com.example.demo.model.UserCreds;
import com.example.demo.repository.UserRepo;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@RestController
	@Log
	@RequiredArgsConstructor
	private static class Controller {

		private final AuthenticationManager authenticationManager;
		private final JwtTokenUtil jwtTokenUtil;

		@GetMapping("/api/hello-user")
		public ResponseEntity<String> hello() {
			log.info("Hello User");
			return ResponseEntity.ok("Hello User");
		}

		@GetMapping("/api/hello-admin")
		public ResponseEntity<String> helloAdmin() {
			log.info("Hello Admin");
			return ResponseEntity.ok("Hello Admin");
		}

		@PostMapping("/login")
		public ResponseEntity<String> login(@RequestBody @Validated UserCreds user) {
			log.info("Login: " + user.getUsername());
			return ResponseEntity.ok("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwidXNlcm5hbWUiOiJqb2huLmRvZSIsImlhdCI6MTUxNjIzOTAyMn0.gxcVvM2lCJFFS26X_ts7LbbG1X4WhKqzwtk6thhWI0o");
		}
	}

	@Component
	static class JwtTokenUtil {

		@Autowired
		private UserRepo userRepo;

		public String getUsernameFromToken(String token) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'getUsernameFromToken'");
		}

		@Bean
		public UserDetailsService userDetailsService() {
			return (String username) -> userRepo.findByUsername(username)
				.orElseThrow(()-> new UsernameNotFoundException("User not found!"));
		}

		@Bean
		public AuthenticationProvider authenticationProvider() {
			DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
			authProvider.setUserDetailsService(userDetailsService());
			authProvider.setPasswordEncoder(passwordEncoder());
			return authProvider;
		}

		@Bean
		public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
			return config.getAuthenticationManager();
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
			return PasswordEncoderFactories.createDelegatingPasswordEncoder();
		}
	}


}

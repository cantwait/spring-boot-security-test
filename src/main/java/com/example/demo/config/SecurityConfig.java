package com.example.demo.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.repository.UserRepo;
import com.example.demo.util.JwtTokenFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // @Value("${jwt.public.key}")
    // private RSAPublicKey rsaPublicKey;

    // @Value("${jwt.private.key}")
    // private RSAPrivateKey rsaPrivateKey;

    private final JwtTokenFilter jwtTokenFilter;
    private final UserRepo userRepo;
    private final AuthenticationProvider authenticationProvider;


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers("/api/hello-admin").hasAnyRole("USER,ADMIN")
                                .requestMatchers("/api/hello-user").hasAnyRole("USER")
                                // ALLOW THE REST
                                .anyRequest().permitAll())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // @Bean
    // public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder
    // passwordEncoder) {
    // var user = User.withUsername("user")
    // .password(passwordEncoder.encode("password"))
    // .roles("USER")
    // .build();
    // var admin = User.withUsername("admin")
    // .password(passwordEncoder.encode("password"))
    // .roles("ADMIN")
    // .build();
    // return new InMemoryUserDetailsManager(user, admin);
    // }

    // @Bean
    // public AuthenticationManager authenticationManagerBean() throws Exception {
    // return super.authenticationManagerBean();
    // }

    // Used by JwtAuthenticationProvider to generate JWT tokens
    // @Bean
    // public JwtEncoder jwtEncoder() {
    //     var jwk = new RSAKey.Builder(this.rsaPublicKey).privateKey(this.rsaPrivateKey).build();
    //     var jwks = new ImmutableJWKSet(new JWKSet(jwk));
    //     return new NimbusJwtEncoder(jwks);
    // }

    // Used by JwtAuthenticationProvider to decode and validate JWT tokens
    // @Bean
    // public JwtDecoder jwtDecoder() {
    //     return NimbusJwtDecoder.withPublicKey(this.rsaPublicKey).build();
    // }

    // Extract authorities from the roles claim
    // @Bean
    // public JwtAuthenticationConverter jwtAuthenticationConverter() {
    //     var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    //     jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
    //     jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

    //     var jwtAuthenticationConverter = new JwtAuthenticationConverter();
    //     jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    //     return jwtAuthenticationConverter;
    // }

    // Expose authentication manager bean
    // @Bean
    // public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
    //         throws Exception {
    //     return authenticationConfiguration.getAuthenticationManager();
    // }

}

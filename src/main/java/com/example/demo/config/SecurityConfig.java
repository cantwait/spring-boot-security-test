package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.filter.JwtTokenFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    // @Value("${jwt.public.key}")
    // private RSAPublicKey rsaPublicKey;

    // @Value("${jwt.private.key}")
    // private RSAPrivateKey rsaPrivateKey;

    private final JwtTokenFilter jwtTokenFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                    .requestMatchers(HttpMethod.POST, "/login")
                    .permitAll()
                )
                .authorizeHttpRequests(
                    authorizeHttpRequests -> authorizeHttpRequests
                    .requestMatchers(HttpMethod.GET, "/api/hello-admin").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/hello-user").hasAnyAuthority("USER", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/**")
                    .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e.accessDeniedHandler(customAccessDeniedHandler))
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

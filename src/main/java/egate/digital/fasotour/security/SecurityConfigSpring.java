package egate.digital.fasotour.security;

import egate.digital.fasotour.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigSpring {

    private static final String AUTH_URL = "/v1/auth/**";
    private static final String ROLES_URL = "/v1/roles/**";
    private static final String CATEGORIES_URL = "/v1/categories/**";
    private static final String LANGUES_URL = "/v1/langues/**";
    private static final String SITES_URL = "/v1/sites/**";
    private static final String TOURISTES_URL = "/v1/auth/touristes/**";
    private static final String GUIDES_URL = "/v1/auth/guides/**";
    private static final String AGENCES_URL = "/v1/auth/agences/**";
    private  static  final String CIRCUITS_URL = "/v1/circuits/**";
    private  static  final String RESERVATIONS_URL = "/v1/reservations/**";
    private  static  final String PAIEMENTS_URL = "/v1/paiements/**";
    private  static  final String AVIS_URL = "/v1/paiements/**";


    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/v1/auth/init-admin").permitAll()

                        .requestMatchers(POST, "/v1/auth/inscription/touriste/**").permitAll()
                        .requestMatchers(POST, "/v1/auth/inscription/guide/**").hasAnyAuthority("ADMIN", "AGENCE")
                        .requestMatchers(POST, "/v1/auth/inscription/agence/**").hasAuthority("ADMIN")

                        //Public
                        .requestMatchers(POST, "/v1/auth/login").permitAll()
                        .requestMatchers(POST, "/v1/auth/activation").permitAll()
                        .requestMatchers(POST, "/v1/auth/renvoi_code").permitAll()
                        .requestMatchers(POST, "/v1/auth/refresh").permitAll()
                        .requestMatchers(POST, "/v1/auth/logout").permitAll()

                        // Role
                        .requestMatchers("/v1/roles/**").hasAuthority("ADMIN")

                        .requestMatchers(GET, "/v1/categories/**").permitAll()
                        .requestMatchers(POST, "/v1/categories/**").hasAuthority("ADMIN")
                        .requestMatchers(PUT, "/v1/categories/**").hasAuthority("ADMIN")
                        .requestMatchers(DELETE, "/v1/categories/**").hasAuthority("ADMIN")

                        .requestMatchers(GET,"/v1/langues/**").permitAll()
                        .requestMatchers(POST, "/v1/langues/**").hasAuthority("ADMIN")
                        .requestMatchers(PUT, "/v1/langues/**").hasAuthority("ADMIN")
                        .requestMatchers(DELETE, "/v1/langues/**").hasAuthority("ADMIN")

                        .requestMatchers(GET, "/v1/sites/**").permitAll()
                        .requestMatchers(POST, "/v1/sites/**").hasAnyAuthority("ADMIN", "AGENCE")
                        .requestMatchers(PUT, "/v1/sites/**").hasAnyAuthority("ADMIN", "AGENCE")
                        .requestMatchers(DELETE, "/v1/sites/**").hasAuthority("ADMIN")

                        .requestMatchers(GET, "/v1/circuits/**").permitAll()
                        .requestMatchers(POST, "/v1/circuits/**").hasAnyAuthority("ADMIN", "AGENCE")
                        .requestMatchers(PUT, "/v1/circuits/**").hasAuthority("AGENCE")
                        .requestMatchers(DELETE, "/v1/circuits/**").hasAuthority("AGENCE")
                        .requestMatchers(PATCH, "/v1/circuits/**").hasAuthority("AGENCE")

                        .requestMatchers( "/v1/reservations/**").hasAnyAuthority("ADMIN", "TOURISTE", "GUIDE", "AGENCE")
                        .requestMatchers( "/v1/paiements/**").hasAnyAuthority("ADMIN", "TOURISTE", "GUIDE", "AGENCE")
                        .requestMatchers( "/v1/avis/**").hasAnyAuthority("ADMIN", "TOURISTE", "GUIDE", "AGENCE")

                        .requestMatchers(GET, "/v1/touristes/**").hasAnyAuthority("ADMIN", "TOURISTE")
                        .requestMatchers(PUT, "/v1/touristes/**").hasAuthority("ADMIN")
                        .requestMatchers(DELETE, "/v1/touristes/**").hasAuthority("ADMIN")
                        .requestMatchers(PATCH, "/v1/touristes/**").hasAuthority("ADMIN")

                        .requestMatchers(GET, "/v1/guides/**").hasAnyAuthority("ADMIN", "GUIDE", "AGENCE")
                        .requestMatchers(PUT, "/v1/guides/**").hasAuthority("ADMIN")
                        .requestMatchers(DELETE, "/v1/guides/**").hasAuthority("ADMIN")
                        .requestMatchers(PATCH, "/v1/guides/**").hasAuthority("ADMIN")

                        .requestMatchers(GET, "/v1/agences/**").hasAnyAuthority("ADMIN", "AGENCE")
                        .requestMatchers(PUT, "/v1/agences/**").hasAuthority("ADMIN")
                        .requestMatchers(DELETE, "/v1/agences/**").hasAuthority("ADMIN")
                        .requestMatchers(PATCH, "/v1/agences/**").hasAuthority("ADMIN")

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    //
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "https://fasotour.bf.com"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "X-XSRF-TOKEN"
        ));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // BScript
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    // Aut

    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
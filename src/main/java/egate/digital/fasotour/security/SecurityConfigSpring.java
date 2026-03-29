package egate.digital.fasotour.security;

import egate.digital.fasotour.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigSpring {

    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/uploads/**").permitAll()
                        // AUTH
                        .requestMatchers(HttpMethod.POST,"/v1/auth/**").permitAll()
                        .requestMatchers("/v1/auth/activation").permitAll()
                        .requestMatchers("/v1/auth/refresh").permitAll()
                        .requestMatchers("/v1/auth/renvoi_code").permitAll()
                        .requestMatchers("/v1/auth/logout").permitAll()

                        // Dashboard
                        .requestMatchers(HttpMethod.GET,"/v1/dashboard/**").permitAll()

                        // Roles
                        .requestMatchers("/v1/roles/**").hasAuthority("ADMIN")

                        // Categories
                        .requestMatchers(HttpMethod.GET,"/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/v1/categories/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/v1/categories/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/v1/categories/**").hasAuthority("ADMIN")

                        // Langues
                        .requestMatchers(HttpMethod.GET,"/v1/langues/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/v1/langues/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/v1/langues/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/v1/langues/**").hasAuthority("ADMIN")

                        // Sites
                        .requestMatchers(HttpMethod.GET,"/v1/sites/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/v1/sites/**").hasAnyAuthority("ADMIN","AGENCE")
                        .requestMatchers(HttpMethod.PUT,"/v1/sites/**").hasAnyAuthority("ADMIN","AGENCE")
                        .requestMatchers(HttpMethod.DELETE,"/v1/sites/**").hasAuthority("ADMIN")

                        // Circuits
                        .requestMatchers(HttpMethod.GET,"/v1/circuits/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/v1/circuits/**").hasAnyAuthority("ADMIN","AGENCE")
                        .requestMatchers(HttpMethod.PUT,"/v1/circuits/**").hasAnyAuthority("ADMIN","AGENCE")
                        .requestMatchers(HttpMethod.DELETE,"/v1/circuits/**").hasAnyAuthority("ADMIN","AGENCE")
                        .requestMatchers(HttpMethod.PATCH,"/v1/circuits/**").hasAnyAuthority("ADMIN","AGENCE")

                        // Reservations
                        .requestMatchers("/v1/reservations/**").hasAnyAuthority("ADMIN","TOURISTE","GUIDE","AGENCE")

                        // Paiements
                        .requestMatchers("/v1/paiements/**")
                        .hasAnyAuthority("ADMIN","TOURISTE","GUIDE","AGENCE")

                        // Avis
                        .requestMatchers("/v1/avis/**")
                        .hasAnyAuthority("ADMIN","TOURISTE","GUIDE","AGENCE")

                        // Touristes
                        .requestMatchers(HttpMethod.GET,"/v1/touristes/**").hasAnyAuthority("ADMIN","TOURISTE")
                        .requestMatchers(HttpMethod.PUT,"/v1/touristes/**").hasAnyAuthority("ADMIN","TOURISTE")
                        .requestMatchers(HttpMethod.DELETE,"/v1/touristes/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,"/v1/touristes/**").hasAuthority("ADMIN")

                        // Guides
                        .requestMatchers(HttpMethod.GET,"/v1/guides/**").hasAnyAuthority("ADMIN","GUIDE","AGENCE")
                        .requestMatchers(HttpMethod.PUT,"/v1/guides/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/v1/guides/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,"/v1/guides/**").hasAuthority("ADMIN")

                        // Agences
                        .requestMatchers(HttpMethod.GET,"/v1/agences/**").hasAnyAuthority("ADMIN","AGENCE")
                        .requestMatchers(HttpMethod.PUT,"/v1/agences/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/v1/agences/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,"/v1/agences/**").hasAuthority("ADMIN")

                        .anyRequest().authenticated()
                )

                .authenticationProvider(authenticationProvider())

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://fasotour.bf.com"
        ));

        config.setAllowedMethods(List.of(
                "GET","POST","PUT","PATCH","DELETE","OPTIONS"
        ));

        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type"
        ));

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }

    // Password encoder
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication provider
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    // Authentication manager
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }
}
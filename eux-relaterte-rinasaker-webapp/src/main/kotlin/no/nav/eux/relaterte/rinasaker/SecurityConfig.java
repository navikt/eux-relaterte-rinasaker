package no.nav.eux.relaterte.rinasaker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests(authz -> authz
//              .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
//              .requestMatchers("/actuator/prometheus").permitAll()
//              .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//              .anyRequest().authenticated()
//            )
//            .oauth2ResourceServer(oauth2 -> oauth2
//                .jwt(Customizer.withDefaults())
//            );
//
//        return http.build();
//    }
}
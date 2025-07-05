package cl.duoc.caasoto.ecomarket.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                            // Permitir acceso público a estas rutas
                            .requestMatchers("/", "/register", "/login", "/css/**", "/js/**", "/h2-console/**").permitAll()
                            // Permitir acceso a todas las demás rutas (ya que manejas la autenticación manualmente)
                            .anyRequest().permitAll()
                    )
                    .csrf(csrf -> csrf
                            // Deshabilitar CSRF para H2 console y para tu aplicación
                            .disable()
                    )
                    .headers(headers -> headers
                            .frameOptions(frameOptions -> frameOptions.sameOrigin())
                    )
                    // Deshabilitar el form login de Spring Security
                    .formLogin(form -> form.disable())
                    // Deshabilitar HTTP Basic authentication
                    .httpBasic(basic -> basic.disable())
                    // Deshabilitar logout automático de Spring Security
                    .logout(logout -> logout.disable());

        return http.build();
    }
}
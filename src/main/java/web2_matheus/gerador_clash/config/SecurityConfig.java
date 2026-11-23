package web2_matheus.gerador_clash.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Recursos estáticos e públicos
                .requestMatchers("/css/**", "/js/**", "/images/**", "/vendors/**", "/webjars/**", "/favicon.ico").permitAll()
                .requestMatchers("/", "/index.html", "/login", "/error/**").permitAll()

                // Visualização de campeonatos e equipes liberada para visitantes
                .requestMatchers(HttpMethod.GET, "/campeonatos", "/campeonatos/*", "/equipes").permitAll()

                // Acesso exclusivo do Administrador para gerenciar usuários (Requisito 8)
                .requestMatchers("/usuarios/**").hasRole("ADMIN")

                // Acesso para Administradores e Organizadores (gerenciar torneios e equipes)
                .requestMatchers("/campeonatos/**").hasAnyRole("ADMIN", "ORGANIZADOR")
                .requestMatchers("/equipes/**").hasAnyRole("ADMIN", "ORGANIZADOR")

                // Qualquer outra rota precisa de autenticação
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/error/403")
            );

        return http.build();
    }
}

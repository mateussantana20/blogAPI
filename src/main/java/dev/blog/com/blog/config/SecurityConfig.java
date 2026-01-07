package dev.blog.com.blog.config;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Gerencia a criptografia das senhas
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Obrigatório para APIs REST
                .authorizeHttpRequests(auth -> auth
                        // Rotas Públicas (Qualquer um acessa)
                        .requestMatchers(HttpMethod.GET, "/posts/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/admins").permitAll() // Liberado para criar o primeiro admin

                        // Rotas Privadas (Exigem Autenticação)
                        .anyRequest().authenticated()
                )
                // Tratamento de Erros Personalizado para o Dashboard
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint()) // Erro 401
                        .accessDeniedHandler(accessDeniedHandler())         // Erro 403
                )
                .httpBasic(withDefaults()); // Permite o login via Postman (Basic Auth)

        return http.build();
    }

    // Gerador de resposta JSON para Erro 401 (Não Autenticado / Senha Errada)
    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"E-mail ou senha inválidos\", \"status\": 401}");
        };
    }

    // Gerador de resposta JSON para Erro 403 (Acesso Proibido)
    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"Você não tem permissão para esta ação\", \"status\": 403}");
        };
    }
}
package dev.blog.com.blog.infra.security;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.blog.com.blog.Admins.AdminModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    // Gera o token quando o Admin faz login
    public String generateToken(AdminModel admin) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("blog-escola-api") // Identifica quem gerou o token
                    .withSubject(admin.getEmail()) // Guarda o e-mail do admin dentro do token
                    .withExpiresAt(genExpirationDate()) // Define o tempo de validade
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    // Valida o token enviado nas requisições POST/PUT/DELETE
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("blog-escola-api")
                    .build()
                    .verify(token)
                    .getSubject(); // Retorna o e-mail do admin se o token for válido
        } catch (JWTVerificationException exception) {
            return ""; // Se for inválido, retorna vazio
        }
    }

    // Define que o token vale por 2 horas
    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
package dev.blog.com.blog.infra.security;
import dev.blog.com.blog.Admins.AdminRepository;
import dev.blog.com.blog.infra.security.dto.AuthenticationDTO;
import dev.blog.com.blog.infra.security.dto.LoginResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AdminRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthenticationController(AdminRepository repository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO data) {
        var admin = this.repository.findByEmail(data.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica se a senha enviada bate com a senha criptografada no banco
        if (passwordEncoder.matches(data.password(), admin.getPassword())) {
            var token = tokenService.generateToken(admin);
            return ResponseEntity.ok(new LoginResponseDTO(token));
        }

        return ResponseEntity.badRequest().build();
    }
}
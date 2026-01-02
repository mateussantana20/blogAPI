package dev.blog.com.blog.Admins;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {
    private final AdminRepository repository; // Injetando a ferramenta que fala com o banco
    private final PasswordEncoder passwordEncoder; // Injetamos o criptografador

    public AdminService(AdminRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    //Listar todos os ninjas
    public List<AdminModel> findAll() {
        return repository.findAll();
    }
    //Criar
    public AdminModel create(AdminModel admin) {
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);
        return repository.save(admin);
    }

    // Listar por iD
    public AdminModel findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public AdminModel update(AdminModel admin, Long id) {
        return repository.findById(id).map(existingAdmin -> {
            admin.setId(id);

            // Verifica se uma nova senha foi enviada no corpo da requisição
            if (admin.getPassword() != null && !admin.getPassword().isBlank()) {
                // Criptografa a nova senha antes de salvar
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            } else {
                // Se não enviou senha nova, mantém a senha que já estava no banco
                admin.setPassword(existingAdmin.getPassword());
            }

            return repository.save(admin);
        }).orElse(null);
    }

    public void deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }
}

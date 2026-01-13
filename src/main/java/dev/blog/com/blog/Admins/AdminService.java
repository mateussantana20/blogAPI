package dev.blog.com.blog.Admins;
import dev.blog.com.blog.services.ImageUploadService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository repository; // Injetando a ferramenta que fala com o banco
    private final PasswordEncoder passwordEncoder; // Injetamos o criptografador
    private final ImageUploadService imageUploadService;

    public AdminService(AdminRepository repository, PasswordEncoder passwordEncoder,  ImageUploadService imageUploadService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.imageUploadService = imageUploadService;
    }

    //Listar todos os ninjas
    public List<AdminModel> findAll() {
        return repository.findAll();
    }

    public AdminModel create(AdminModel admin, String imageUrl) {
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);
        admin.setProfilePicture(imageUrl); // Salva a URL da foto de perfil
        return repository.save(admin);
    }

    // Listar por iD
    public AdminModel findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public AdminModel update(AdminModel admin, Long id, String newImageUrl) {
        return repository.findById(id).map(existingAdmin -> {
            admin.setId(id);
            if (admin.getPassword() != null && !admin.getPassword().isBlank()) {
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            } else {
                admin.setPassword(existingAdmin.getPassword());
            }
            if (newImageUrl != null) {
                if (existingAdmin.getProfilePicture() != null) {
                    try {
                        imageUploadService.deleteImage(existingAdmin.getProfilePicture(), "perfil");
                    } catch (Exception e) {
                        System.out.println("Erro ao deletar imagem antiga: " + e.getMessage());
                    }
                }
                admin.setProfilePicture(newImageUrl);
            } else {
                admin.setProfilePicture(existingAdmin.getProfilePicture());
            }
            return repository.save(admin);
        }).orElse(null);
    }

    public void deleteById(Long id) {
        Optional<AdminModel> admin = repository.findById(id);
        if (admin.isPresent()) {
            try {
                if (admin.get().getProfilePicture() != null) {
                    imageUploadService.deleteImage(admin.get().getProfilePicture(), "perfil");
                }
                repository.deleteById(id);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao deletar foto de perfil: " + e.getMessage());
            }
        }
    }
}

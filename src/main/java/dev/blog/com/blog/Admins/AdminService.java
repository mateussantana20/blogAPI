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

    public AdminModel update(AdminModel inputData, Long id, String newImageUrl) {
        return repository.findById(id).map(existingAdmin -> {

            // Atualiza apenas se o dado foi enviado (evita null)
            if (inputData.getName() != null) existingAdmin.setName(inputData.getName());
            if (inputData.getBio() != null) existingAdmin.setBio(inputData.getBio());
            if (inputData.getEmail() != null) existingAdmin.setEmail(inputData.getEmail());

            // Lógica de Senha
            if (inputData.getPassword() != null && !inputData.getPassword().isBlank()) {
                existingAdmin.setPassword(passwordEncoder.encode(inputData.getPassword()));
            }

            // Lógica de Imagem
            if (newImageUrl != null) {
                // Deleta antiga se existir
                if (existingAdmin.getProfilePicture() != null) {
                    try {
                        imageUploadService.deleteImage(existingAdmin.getProfilePicture(), "perfil");
                    } catch (Exception e) {
                        System.out.println("Erro ao deletar imagem antiga: " + e.getMessage());
                    }
                }
                existingAdmin.setProfilePicture(newImageUrl);
            }
            // Se newImageUrl for null, mantém a antiga (não faz nada)

            return repository.save(existingAdmin);
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

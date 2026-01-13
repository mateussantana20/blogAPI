package dev.blog.com.blog.Posts;
import dev.blog.com.blog.Admins.AdminModel;
import dev.blog.com.blog.Admins.AdminRepository;
import dev.blog.com.blog.services.ImageUploadService;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class PostsService {

    private final PostsRepository repository;
    private final AdminRepository adminRepository; // Injetado para validar o autor
    private final ImageUploadService imageUploadService;

    public PostsService(PostsRepository repository, AdminRepository adminRepository,  ImageUploadService imageUploadService) {
        this.repository = repository;
        this.adminRepository = adminRepository;
        this.imageUploadService = imageUploadService;
    }
    public List<PostsModel> findAll() {
        return repository.findAll();
    }

    public PostsModel findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public PostsModel save(PostsModel post) {
        if (post.getAdmin() == null || post.getAdmin().getId() == null) {
            throw new RuntimeException("Erro: Todo post precisa de um administrador (admin_id) associado.");
        }
        AdminModel admin = adminRepository.findById(post.getAdmin().getId())
                .orElseThrow(() -> new RuntimeException("Erro: O Administrador com ID " + post.getAdmin().getId() + " não existe."));

        post.setAdmin(admin);
        if (post.getDataPublication() == null) {
            post.setDataPublication(LocalDateTime.now());
        }
        return repository.save(post);
    }
    public PostsModel update(PostsModel post, Long id) {
        return repository.findById(id).map(existingPost -> {
            post.setId(id);

            if (post.getDataPublication() == null) {
                post.setDataPublication(existingPost.getDataPublication());
            }
            if (post.getAdmin() != null && post.getAdmin().getId() != null) {
                AdminModel admin = adminRepository.findById(post.getAdmin().getId())
                        .orElseThrow(() -> new RuntimeException("Admin não encontrado para atualização."));
                post.setAdmin(admin);
            }
            return repository.save(post);
        }).orElse(null);
    }

    public void deleteById(Long id) {
        Optional<PostsModel> post = repository.findById(id);
        if (post.isPresent()) {
            try {
                if (post.get().getImageUrl() != null) {
                    imageUploadService.deleteImage(post.get().getImageUrl(), "posts");
                }
                repository.deleteById(id);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao deletar imagem do Cloudinary: " + e.getMessage());
            }
        }
    }
    public Page<PostsModel> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
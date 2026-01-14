package dev.blog.com.blog.Posts;
import dev.blog.com.blog.Admins.AdminModel;
import dev.blog.com.blog.Admins.AdminRepository;
import dev.blog.com.blog.Categories.CategoryModel;
import dev.blog.com.blog.Categories.CategoryRepository; // Importação necessária
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
    private final AdminRepository adminRepository;
    private final CategoryRepository categoryRepository; // Injetado para buscar os dados da categoria
    private final ImageUploadService imageUploadService;

    public PostsService(PostsRepository repository,
                        AdminRepository adminRepository,
                        CategoryRepository categoryRepository,
                        ImageUploadService imageUploadService) {
        this.repository = repository;
        this.adminRepository = adminRepository;
        this.categoryRepository = categoryRepository;
        this.imageUploadService = imageUploadService;
    }

    public List<PostsModel> findAll() {
        return repository.findAll();
    }

    public PostsModel findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public PostsModel save(PostsModel post) {
        // 1. Validar e associar o Admin
        if (post.getAdmin() == null || post.getAdmin().getId() == null) {
            throw new RuntimeException("Erro: Todo post precisa de um administrador associado.");
        }
        AdminModel admin = adminRepository.findById(post.getAdmin().getId())
                .orElseThrow(() -> new RuntimeException("Erro: O Administrador não existe."));
        post.setAdmin(admin);

        // 2. BUSCA A CATEGORIA NO BANCO (Para evitar o categoryName: null no JSON)
        if (post.getCategory() != null && post.getCategory().getId() != null) {
            CategoryModel category = categoryRepository.findById(post.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Erro: Categoria não encontrada."));
            post.setCategory(category);
        }

        if (post.getDataPublication() == null) {
            post.setDataPublication(LocalDateTime.now());
        }
        return repository.save(post);
    }

    public PostsModel update(PostsModel post, Long id, String newImageUrl) {
        return repository.findById(id).map(existingPost -> {
            post.setId(id);

            if (post.getDataPublication() == null) {
                post.setDataPublication(existingPost.getDataPublication());
            }

            // Se uma nova imagem foi enviada, deleta a antiga e define a nova
            if (newImageUrl != null) {
                if (existingPost.getImageUrl() != null) {
                    try {
                        imageUploadService.deleteImage(existingPost.getImageUrl(), "posts");
                    } catch (Exception e) {
                        System.out.println("Erro ao deletar imagem antiga: " + e.getMessage());
                    }
                }
                post.setImageUrl(newImageUrl);
            } else {
                // Se não enviou foto nova, mantém a que já estava
                post.setImageUrl(existingPost.getImageUrl());
            }

            // Mantém as associações de Admin e Categoria
            if (post.getAdmin() != null && post.getAdmin().getId() != null) {
                AdminModel admin = adminRepository.findById(post.getAdmin().getId())
                        .orElseThrow(() -> new RuntimeException("Admin não encontrado."));
                post.setAdmin(admin);
            }

            if (post.getCategory() != null && post.getCategory().getId() != null) {
                CategoryModel category = categoryRepository.findById(post.getCategory().getId())
                        .orElseThrow(() -> new RuntimeException("Categoria não encontrada."));
                post.setCategory(category);
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
package dev.blog.com.blog.Posts;

import dev.blog.com.blog.Admins.AdminModel;
import dev.blog.com.blog.Admins.AdminRepository;
import dev.blog.com.blog.Categories.CategoryModel;
import dev.blog.com.blog.Categories.CategoryRepository;
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
    private final CategoryRepository categoryRepository;
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

    public Page<PostsModel> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public PostsModel findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public PostsModel save(PostsModel post) {
        if (post.getAdmin() == null || post.getAdmin().getId() == null) {
            throw new RuntimeException("Erro: Todo post precisa de um administrador associado.");
        }

        AdminModel admin = adminRepository.findById(post.getAdmin().getId())
                .orElseThrow(() -> new RuntimeException("Erro: O Administrador não existe."));
        post.setAdmin(admin);

        // 2. Busca Categoria
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

    public PostsModel update(PostsModel postData, Long id, String newImageUrl) {
        return repository.findById(id).map(existingPost -> {

            existingPost.setTitle(postData.getTitle());
            existingPost.setContent(postData.getContent());

            if (postData.getCategory() != null && postData.getCategory().getId() != null) {
                CategoryModel category = categoryRepository.findById(postData.getCategory().getId())
                        .orElseThrow(() -> new RuntimeException("Categoria não encontrada."));
                existingPost.setCategory(category);
            }

            if (newImageUrl != null) {
                if (existingPost.getImageUrl() != null && !existingPost.getImageUrl().startsWith("http")) {
                    try {
                        imageUploadService.deleteImage(existingPost.getImageUrl(), "posts");
                    } catch (Exception e) {
                        System.out.println("Erro ao deletar imagem antiga: " + e.getMessage());
                    }
                }
                existingPost.setImageUrl(newImageUrl);
            }
            return repository.save(existingPost);
        }).orElse(null);
    }

    public void deleteById(Long id) {
        Optional<PostsModel> post = repository.findById(id);
        if (post.isPresent()) {
            try {
                if (post.get().getImageUrl() != null && !post.get().getImageUrl().startsWith("http")) {
                    imageUploadService.deleteImage(post.get().getImageUrl(), "posts");
                }
                repository.deleteById(id);
            } catch (Exception e) {
                // Mudamos para apenas logar erro de imagem, mas deletar o post mesmo assim
                System.err.println("Aviso: Erro ao deletar imagem, mas prosseguindo com delete do post: " + e.getMessage());
                repository.deleteById(id);
            }
        }
    }

    // Método de busca
    public Page<PostsModel> searchByTitle(String title, Pageable pageable) {
        return repository.findByTitleContainingIgnoreCase(title, pageable);
    }
}
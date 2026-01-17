package dev.blog.com.blog.Posts;

import dev.blog.com.blog.Admins.AdminModel;
import dev.blog.com.blog.Admins.AdminRepository;
import dev.blog.com.blog.services.ImageUploadService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/posts")
public class PostsController {

    private final PostsService service;
    private final ImageUploadService imageUploadService;
    private final PostsRepository postsRepository;
    private final AdminRepository adminRepository;

    public PostsController(PostsService service,
                           ImageUploadService imageUploadService,
                           PostsRepository postsRepository,
                           AdminRepository adminRepository) {
        this.service = service;
        this.imageUploadService = imageUploadService;
        this.postsRepository = postsRepository;
        this.adminRepository = adminRepository;
    }

    // --- CRIAR POST (Aceita Arquivo ou URL) ---
    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<PostDTO> create(
            @RequestPart("post") @Valid PostsModel post,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Principal principal
    ) throws IOException {

        // 1. SEGURANÇA: Definir autor pelo Token
        String emailLogado = principal.getName();
        AdminModel autor = adminRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new RuntimeException("Erro: Usuário logado não encontrado no banco."));
        post.setAdmin(autor);

        // 2. LÓGICA DE IMAGEM HÍBRIDA
        // Prioridade A: Se enviou arquivo, faz upload e sobrescreve a URL
        if (file != null && !file.isEmpty()) {
            String imageUrl = imageUploadService.uploadImage(file, "posts");
            post.setImageUrl(imageUrl);
        }
        // Prioridade B: Se não enviou arquivo, o Java mantém o post.getImageUrl()
        // que veio do JSON (Link externo), se o usuário tiver preenchido no Front.

        PostsModel savedPost = service.save(post);
        return ResponseEntity.status(201).body(new PostDTO(savedPost));
    }

    // --- ATUALIZAR POST (Aceita Arquivo ou URL) ---
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<PostDTO> update(
            @PathVariable Long id,
            @RequestPart("post") @Valid PostsModel post,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        String newImageUrl = null;

        // Prioridade 1: Usuário enviou um novo Arquivo (Upload)
        if (file != null && !file.isEmpty()) {
            newImageUrl = imageUploadService.uploadImage(file, "posts");
        }
        // Prioridade 2: Usuário enviou uma URL externa (Link)
        else if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            newImageUrl = post.getImageUrl();
        }
        // Se ambos forem nulos, o Service deve manter a imagem antiga

        PostsModel updatedPost = service.update(post, id, newImageUrl);

        if (updatedPost != null) {
            return ResponseEntity.ok(new PostDTO(updatedPost));
        }
        return ResponseEntity.notFound().build();
    }

    // --- OUTROS MÉTODOS (Mantidos iguais) ---

    @GetMapping
    public ResponseEntity<Page<PostDTO>> findAll(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDTO> posts = service.findAll(pageable).map(PostDTO::new);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> findById(@PathVariable Long id) {
        PostsModel post = service.findById(id);
        if (post != null) {
            return ResponseEntity.ok(new PostDTO(post));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/category/{slug}")
    public ResponseEntity<Page<PostDTO>> findByCategory(@PathVariable String slug, Pageable pageable) {
        Page<PostDTO> posts = postsRepository.findByCategorySlug(slug, pageable).map(PostDTO::new);
        return ResponseEntity.ok(posts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PostDTO>> searchByTitle(
            @RequestParam("title") String title,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDTO> posts = postsRepository.findByTitleContainingIgnoreCase(title, pageable).map(PostDTO::new);
        return ResponseEntity.ok(posts);
    }
}
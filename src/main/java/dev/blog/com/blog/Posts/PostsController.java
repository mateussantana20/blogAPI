package dev.blog.com.blog.Posts;
import dev.blog.com.blog.services.ImageUploadService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
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

    public PostsController(PostsService service, ImageUploadService imageUploadService, PostsRepository postsRepository) {
        this.service = service;
        this.imageUploadService = imageUploadService;
        this.postsRepository = postsRepository;
    }

    // Criar post com Imagem
    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<PostDTO> create(
            @RequestPart("post") @Valid PostsModel post,
            @RequestPart("file") MultipartFile file) throws IOException {

        String imageUrl = imageUploadService.uploadImage(file, "posts");
        post.setImageUrl(imageUrl);

        PostsModel savedPost = service.save(post);
        return ResponseEntity.status(201).body(new PostDTO(savedPost));
    }

    // Listar todos com Paginação e DTO
    @GetMapping
    public ResponseEntity<Page<PostDTO>> findAll(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        // Altere de Page<PostsModel> para Page<PostDTO>
        Page<PostDTO> posts = service.findAll(pageable).map(PostDTO::new);
        return ResponseEntity.ok(posts);
    }

    // Buscar por ID com DTO
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> findById(@PathVariable Long id) {
        PostsModel post = service.findById(id);
        if (post != null) {
            return ResponseEntity.ok(new PostDTO(post));
        }
        return ResponseEntity.notFound().build();
    }

    // Filtrar por Categoria Slug
    @GetMapping("/category/{slug}")
    public ResponseEntity<Page<PostDTO>> findByCategory(@PathVariable String slug, Pageable pageable) {
        Page<PostDTO> posts = postsRepository.findByCategorySlug(slug, pageable).map(PostDTO::new);
        return ResponseEntity.ok(posts);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<PostDTO> update(
            @PathVariable Long id,
            @RequestPart("post") @Valid PostsModel post,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        String newImageUrl = null;
        if (file != null && !file.isEmpty()) {
            // Faz upload da nova imagem para a pasta "posts"
            newImageUrl = imageUploadService.uploadImage(file, "posts");
        }

        // Passamos a nova URL para o service lidar com a exclusão da antiga
        PostsModel updatedPost = service.update(post, id, newImageUrl);

        if (updatedPost != null) {
            return ResponseEntity.ok(new PostDTO(updatedPost));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
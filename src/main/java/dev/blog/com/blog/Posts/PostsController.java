package dev.blog.com.blog.Posts;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/posts")
public class PostsController {
    private final PostsService service;

    public PostsController(PostsService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PostsModel> create(@Valid @RequestBody PostsModel post) {
        PostsModel savedPost = service.save(post);
        return ResponseEntity.status(201).body(savedPost);
    }

    @GetMapping
    public List<PostsModel> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostsModel> findById(@PathVariable Long id) {
        PostsModel post = service.findById(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostsModel> update(@Valid @RequestBody PostsModel post, @PathVariable Long id) {
        PostsModel updatedPost = service.update(post, id);
        if (updatedPost != null) {
            return ResponseEntity.ok(updatedPost);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<PostsModel>> findAll(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(service.findAll(pageable));
    }
}

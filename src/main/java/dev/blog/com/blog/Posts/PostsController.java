package dev.blog.com.blog.Posts;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostsController {
    private final PostsService service;

    public PostsController(PostsService service) {
        this.service = service;
    }

    @PostMapping
    public PostsModel create(@RequestBody PostsModel post ) {
        return service.save(post);
    }

    @GetMapping
    public List<PostsModel> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public PostsModel findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public PostsModel update(@RequestBody PostsModel post, @PathVariable Long id) {
       return service.update(post, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
}

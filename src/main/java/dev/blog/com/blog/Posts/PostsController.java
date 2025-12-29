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
    public String create() {
        return "Post created successfully";
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
    public String update(@PathVariable Long id) {
        return "Post with ID " + id + " updated";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return "Post with ID " + id + " deleted";
    }
}

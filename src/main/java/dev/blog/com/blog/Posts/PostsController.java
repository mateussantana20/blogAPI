package dev.blog.com.blog.Posts;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostsController {
    @PostMapping
    public String create() {
        return "Post created successfully";
    }
    @GetMapping
    public String findAll() {
        return "List of all Post";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id) {
        return "Post with id: " + id;
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

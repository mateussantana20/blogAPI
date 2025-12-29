package dev.blog.com.blog.Posts;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PostsService {

    private final PostsRepository repository;
    public PostsService(PostsRepository repository) {
        this.repository = repository;
    }

    public List<PostsModel> findAll () {
        return repository.findAll();
    }

    public PostsModel findById(Long id) {
        Optional<PostsModel> post = repository.findById(id);
        return post.orElse(null);
    }
}

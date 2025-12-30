package dev.blog.com.blog.Posts;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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

    public PostsModel update(PostsModel post, Long id) {
        return repository.findById(id).map(existingPost -> {
            post.setId(id);
            return repository.save(post);
        }).orElse(null);
    }

    public PostsModel save(PostsModel post) {
        // Se não vier data, coloca a data de agora antes de salvar
        if (post.getDataPublication() == null) {
            post.setDataPublication(java.time.LocalDateTime.now());
        }
        return repository.save(post);
    }

    public void deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }
}

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

    public PostsModel update (PostsModel post, Long id) {
        Optional<PostsModel> postsModel = repository.findById(id);
        if (postsModel.isPresent()) {
            PostsModel updated = postsModel.get();
            return repository.save(updated);
        }
        return null;
    }

    public PostsModel save(PostsModel post) {
        return repository.save(post);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

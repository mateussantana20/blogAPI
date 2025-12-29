package dev.blog.com.blog.Posts;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostsService {

    private final PostsRepository repository;
    public PostsService(PostsRepository repository) {
        this.repository = repository;
    }

    //Listar todos os ninjas
    public List<PostsModel> findAll () {
        return repository.findAll();
    }


}

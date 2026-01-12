package dev.blog.com.blog.Posts;
import dev.blog.com.blog.Admins.AdminModel;
import dev.blog.com.blog.Admins.AdminRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
public class PostsService {

    private final PostsRepository repository;
    private final AdminRepository adminRepository; // Injetado para validar o autor

    public PostsService(PostsRepository repository, AdminRepository adminRepository) {
        this.repository = repository;
        this.adminRepository = adminRepository;
    }

    public List<PostsModel> findAll() {
        return repository.findAll();
    }

    public PostsModel findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public PostsModel save(PostsModel post) {
        // 1. Validação de segurança: O post tem um objeto admin e um ID?
        if (post.getAdmin() == null || post.getAdmin().getId() == null) {
            throw new RuntimeException("Erro: Todo post precisa de um administrador (admin_id) associado.");
        }

        // 2. Validação de existência: Esse ID de admin existe no banco?
        AdminModel admin = adminRepository.findById(post.getAdmin().getId())
                .orElseThrow(() -> new RuntimeException("Erro: O Administrador com ID " + post.getAdmin().getId() + " não existe."));

        // 3. Vinculação: Associamos o admin real encontrado ao post
        post.setAdmin(admin);

        // 4. Automação: Se não vier data, coloca a data de agora
        if (post.getDataPublication() == null) {
            post.setDataPublication(LocalDateTime.now());
        }

        return repository.save(post);
    }

    public PostsModel update(PostsModel post, Long id) {
        return repository.findById(id).map(existingPost -> {
            post.setId(id);

            // No update, também garantimos que a data original não se perca se não enviada
            if (post.getDataPublication() == null) {
                post.setDataPublication(existingPost.getDataPublication());
            }

            // Validamos o admin também no update para evitar trocar para um admin inexistente
            if (post.getAdmin() != null && post.getAdmin().getId() != null) {
                AdminModel admin = adminRepository.findById(post.getAdmin().getId())
                        .orElseThrow(() -> new RuntimeException("Admin não encontrado para atualização."));
                post.setAdmin(admin);
            }

            return repository.save(post);
        }).orElse(null);
    }

    public void deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    public Page<PostsModel> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
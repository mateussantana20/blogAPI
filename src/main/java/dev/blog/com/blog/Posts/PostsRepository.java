package dev.blog.com.blog.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<PostsModel, Long> {
    // Busca por categoria (você já tem este)
    Page<PostsModel> findByCategorySlug(String slug, Pageable pageable);

    // NOVO: Busca posts onde o título contém o termo pesquisado
    Page<PostsModel> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
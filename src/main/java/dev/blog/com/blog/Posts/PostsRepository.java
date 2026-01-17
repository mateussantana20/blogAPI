package dev.blog.com.blog.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsRepository extends JpaRepository<PostsModel, Long> {

    // Busca posts por slug (já existia)
    Page<PostsModel> findByCategorySlug(String slug, Pageable pageable);

    // Busca posts por título (já existia)
    Page<PostsModel> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // --- NOVO: Busca lista de posts pelo ID da categoria (Para o delete funcionar) ---
    List<PostsModel> findByCategory_Id(Long categoryId);
}
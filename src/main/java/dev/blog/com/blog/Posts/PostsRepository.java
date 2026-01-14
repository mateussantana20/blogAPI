package dev.blog.com.blog.Posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<PostsModel, Long> {
    // Busca posts que pertencem a uma categoria específica pelo Slug
    Page<PostsModel> findByCategorySlug(String slug, Pageable pageable);
}
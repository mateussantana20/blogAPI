package dev.blog.com.blog.Posts;
import dev.blog.com.blog.Admins.AdminDTO;
import java.time.LocalDateTime; // <--- Importante importar isso

public record PostDTO(
        Long id,
        String title,
        String content,
        String imageUrl,
        String categoryName,
        String categorySlug,
        Long categoryId, // <--- NOVO CAMPO
        AdminDTO author,
        LocalDateTime dataPublication // <--- 1. Adicione este campo
) {
    public PostDTO(PostsModel post) {
        this(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.getCategory() != null ? post.getCategory().getName() : "Sem Categoria",
                post.getCategory() != null ? post.getCategory().getSlug() : "geral",
                post.getCategory() != null ? post.getCategory().getId() : null, // <--- PREENCHE O ID
                post.getAdmin() != null ? new AdminDTO(post.getAdmin()) : null,
                post.getDataPublication()
        );
    }
}
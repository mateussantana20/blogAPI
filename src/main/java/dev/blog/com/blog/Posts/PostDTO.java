package dev.blog.com.blog.Posts;
import dev.blog.com.blog.Admins.AdminDTO;

public record PostDTO(
        Long id,
        String title,
        String content,
        String imageUrl,
        String categoryName,
        String categorySlug,
        AdminDTO author
) {
    public PostDTO(PostsModel post) {
        this(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.getCategory() != null ? post.getCategory().getName() : "Sem Categoria",
                post.getCategory() != null ? post.getCategory().getSlug() : "geral",
                post.getAdmin() != null ? new AdminDTO(post.getAdmin()) : null // PROTEÇÃO AQUI
        );
    }
}
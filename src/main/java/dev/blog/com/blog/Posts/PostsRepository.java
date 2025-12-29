package dev.blog.com.blog.Posts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<PostsModel, Long> {
}

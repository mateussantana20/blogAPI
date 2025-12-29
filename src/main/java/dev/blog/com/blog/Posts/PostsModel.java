package dev.blog.com.blog.Posts;
import dev.blog.com.blog.Admins.AdminModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_posts")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String imageUrl;

    @CreationTimestamp
    private LocalDateTime dataPublication;
    private String summary;
    private String author;
    private Boolean status;

//    Muitos posts para um adm;
    @ManyToOne
    @JoinColumn(name = "admin_id") // Boa prática: define o nome da coluna da FK
    private AdminModel admin;

}

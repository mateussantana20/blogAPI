package dev.blog.com.blog.Posts;
import dev.blog.com.blog.Admins.AdminModel;
import dev.blog.com.blog.Categories.CategoryModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "O título é obrigatório")
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "O conteúdo não pode estar vazio")
    private String content;
    private String imageUrl;

    @CreationTimestamp
    private LocalDateTime dataPublication;
    private String summary;
    private Boolean status;

//    Muitos posts para um adm;
    @ManyToOne
    @JoinColumn(name = "admin_id") // Boa prática: define o nome da coluna da FK
    private AdminModel admin;

    // Adicione estes campos dentro da sua classe PostsModel
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryModel category;
}

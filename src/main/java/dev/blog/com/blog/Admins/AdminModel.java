package dev.blog.com.blog.Admins;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.blog.com.blog.Posts.PostsModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Diz que isso é uma entidade para o banco de dados;
@Entity
@Table(name = "tb_admin")
@AllArgsConstructor// Lombok
@NoArgsConstructor // Lombok
@Data // Lombok - cria os Getters e os Setters
public class AdminModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(unique = true)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Column(name = "profile_picture") // Garante a conexão com o SQL
    private String profilePicture;
    private String bio;

    @OneToMany(mappedBy = "admin")
    @JsonIgnore
    private List<PostsModel> posts;
}

package dev.blog.com.blog.Admins;
import dev.blog.com.blog.Posts.PostsModel;
import jakarta.persistence.*;
import java.util.List;

// Diz que isso é uma entidade para o banco de dados;
@Entity
@Table(name = "tb_admin")
public class AdminModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String profilePicture;
    private String bio;

    @OneToMany(mappedBy = "admin")
    private List<PostsModel> posts;

    public AdminModel() {
    }

    public AdminModel(String name, String email, String password, String profilePicture, String bio) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}

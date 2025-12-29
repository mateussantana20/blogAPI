package dev.blog.com.blog.Posts;
import dev.blog.com.blog.Admins.AdminModel;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_posts")
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

//    Muitos posts um adm;
    @ManyToOne
    @JoinColumn(name = "admin_id") // Boa prática: define o nome da coluna da FK
    private AdminModel admin;


    public PostsModel() {}

    public PostsModel(String title, String content, String imageUrl, LocalDateTime dataPublication, String summary, String author, Boolean status) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.dataPublication = dataPublication;
        this.summary = summary;
        this.author = author;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getDataPublication() {
        return dataPublication;
    }

    public void setDataPublication(LocalDateTime dataPublication) {
        this.dataPublication = dataPublication;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}

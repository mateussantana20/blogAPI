package dev.blog.com.blog.Categories;

import dev.blog.com.blog.Posts.PostsModel;
import dev.blog.com.blog.Posts.PostsRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository repository;
    private final PostsRepository postRepository;

    public CategoryController(CategoryRepository repository, PostsRepository postRepository) {
        this.repository = repository;
        this.postRepository = postRepository;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody CategoryModel category) {
        try {
            if (category.getSlug() == null || category.getSlug().isEmpty()) {
                category.setSlug(toSlug(category.getName()));
            }
            return ResponseEntity.status(201).body(repository.save(category));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Erro: Já existe uma categoria com este nome.");
        }
    }

    @GetMapping
    public ResponseEntity<List<CategoryModel>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CategoryModel categoryData) {
        System.out.println("--- INICIANDO UPDATE ---");
        System.out.println("ID recebido na URL: " + id);
        System.out.println("Novo Nome recebido: " + categoryData.getName());

        return repository.findById(id).map(existingCategory -> {
            try {
                existingCategory.setName(categoryData.getName());
                existingCategory.setSlug(toSlug(categoryData.getName()));

                CategoryModel updated = repository.save(existingCategory);
                System.out.println("Sucesso! Categoria salva.");
                return ResponseEntity.ok(updated);
            } catch (DataIntegrityViolationException e) {
                System.out.println("Erro: Nome duplicado no banco.");
                return ResponseEntity.badRequest().body("Erro: Este nome de categoria já existe.");
            } catch (Exception e) {
                e.printStackTrace(); // Mostra o erro real no console
                return ResponseEntity.internalServerError().body("Erro interno: " + e.getMessage());
            }
        }).orElseGet(() -> {
            System.out.println("Erro: ID " + id + " não encontrado no banco.");
            return ResponseEntity.notFound().build();
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // Desvincula posts
        List<PostsModel> postsDaCategoria = postRepository.findByCategory_Id(id);
        for (PostsModel post : postsDaCategoria) {
            post.setCategory(null);
            postRepository.save(post);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private String toSlug(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String noAccents = Pattern.compile("[\\p{InCombiningDiacriticalMarks}]").matcher(normalized).replaceAll("");
        String slug = noAccents.replace(" ", "-").replaceAll("[^a-zA-Z0-9-]", "").toLowerCase();
        return slug;
    }
}
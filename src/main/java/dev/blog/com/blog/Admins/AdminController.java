package dev.blog.com.blog.Admins;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Essa anotation diz que essa classe é um controller
@RestController
//Mapeia as rotas
@RequestMapping("/admins")
public class AdminController {
    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    //GET
    @GetMapping("/boasvinda")
    public String boasVindas() {
        return "Bem-vindo ao sistema de Administração!";
    }

    // Adicionar admin
    @PostMapping
    public ResponseEntity<AdminModel> create(@Valid @RequestBody AdminModel admin) {
        AdminModel createdAdmin = service.create(admin);
        return ResponseEntity.status(201).body(createdAdmin);
    }

    // Mostrar todos os admins
    @GetMapping
    public ResponseEntity<List<AdminModel>> findAll() {
        List<AdminModel> admins = service.findAll();
        return ResponseEntity.ok(admins); // Status 200 OK
    }

    // Procurar Admin por ID
    @GetMapping("/{id}")
    public ResponseEntity<AdminModel> findById(@PathVariable Long id) {
        AdminModel admin = service.findById(id);
        if (admin != null) {
            return ResponseEntity.ok(admin); // Status 200 OK
        }
        return ResponseEntity.notFound().build(); // Status 404 Not Found
    }

    // Alterar dados do admin
    @PutMapping("/{id}")
    public ResponseEntity<AdminModel> update(@Valid @PathVariable Long id, @RequestBody AdminModel admin) {
        AdminModel updatedAdmin = service.update(admin, id);
        if (updatedAdmin != null) {
            return ResponseEntity.ok(updatedAdmin); // Status 200 OK
        }
        return ResponseEntity.notFound().build(); // Status 404 Not Found
    }

    // Deletar admin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build(); // Status 204 No Content
    }
}

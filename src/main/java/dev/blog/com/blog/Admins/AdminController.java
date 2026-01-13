package dev.blog.com.blog.Admins;
import dev.blog.com.blog.services.ImageUploadService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

// Essa anotation diz que essa classe é um controller
@RestController
//Mapeia as rotas
@RequestMapping("/admins")
public class AdminController {
    private final AdminService service;
    private final ImageUploadService imageUploadService;

    public AdminController(AdminService service, ImageUploadService imageUploadService) {
        this.service = service;
        this.imageUploadService = imageUploadService;
    }

    @GetMapping("/boasvinda")
    public String boasVindas() {
        return "Bem-vindo ao sistema de Administração!";
    }

    // Adicionar admin
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AdminModel> create(
            @RequestPart("admin") @Valid AdminModel admin,
            @RequestPart("file") MultipartFile file) throws IOException {

        // 1. Usa o método específico de perfil (com o corte automático)
        String imageUrl = imageUploadService.uploadProfilePicture(file);

        // 2. Salva o admin com a URL da foto cortada
        AdminModel createdAdmin = service.create(admin, imageUrl);

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
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AdminModel> update(
            @PathVariable Long id,
            @RequestPart("admin") @Valid AdminModel admin,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        String newImageUrl = null;
        if (file != null && !file.isEmpty()) {
            newImageUrl = imageUploadService.uploadProfilePicture(file);
        }
        AdminModel updatedAdmin = service.update(admin, id, newImageUrl);
        if (updatedAdmin != null) {
            return ResponseEntity.ok(updatedAdmin);
        }
        return ResponseEntity.notFound().build();
    }

    // Deletar admin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build(); // Status 204 No Content
    }
}

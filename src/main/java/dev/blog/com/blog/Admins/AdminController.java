package dev.blog.com.blog.Admins;
import dev.blog.com.blog.services.ImageUploadService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admins")
public class AdminController {
    private final AdminService service;
    private final ImageUploadService imageUploadService;

    public AdminController(AdminService service, ImageUploadService imageUploadService) {
        this.service = service;
        this.imageUploadService = imageUploadService;
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AdminDTO> create(
            @RequestPart("admin") @Valid AdminModel admin,
            @RequestPart("file") MultipartFile file) throws IOException {

        String imageUrl = imageUploadService.uploadProfilePicture(file);
        AdminModel createdAdmin = service.create(admin, imageUrl);
        return ResponseEntity.status(201).body(new AdminDTO(createdAdmin));
    }

    @GetMapping
    public ResponseEntity<List<AdminDTO>> findAll() {
        // Converte a lista de Models para DTOs
        List<AdminDTO> admins = service.findAll()
                .stream()
                .map(AdminDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> findById(@PathVariable Long id) {
        AdminModel admin = service.findById(id);
        if (admin != null) {
            return ResponseEntity.ok(new AdminDTO(admin));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AdminDTO> update(
            @PathVariable Long id,
            @RequestPart("admin") @Valid AdminModel admin,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        String newImageUrl = null;
        if (file != null && !file.isEmpty()) {
            newImageUrl = imageUploadService.uploadProfilePicture(file);
        }

        AdminModel updatedAdmin = service.update(admin, id, newImageUrl);
        if (updatedAdmin != null) {
            return ResponseEntity.ok(new AdminDTO(updatedAdmin));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
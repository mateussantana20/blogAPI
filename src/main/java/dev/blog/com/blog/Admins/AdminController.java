package dev.blog.com.blog.Admins;
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
    public AdminModel create(@RequestBody AdminModel admin) {
        return service.create(admin);
    }

    // Mostrar todos os admins
    @GetMapping
    public List<AdminModel> findAll() {
        return service.findAll();
    }

    // Procurar Admin por ID
    @GetMapping("/{id}")
    public AdminModel findById(@PathVariable Long id) {
        return service.findById(id);
    }

    // Alterar dados do admin
    @PutMapping("/{id}")
    public String update(@PathVariable Long id) {
        return "Admin with ID " + id + " updated";
    }

    // Deletar admin
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return "Admin with ID " + id + " deleted";
    }
}

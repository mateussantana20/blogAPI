package dev.blog.com.blog.Admins;
import org.springframework.web.bind.annotation.*;

// Essa anotation diz que essa classe é um controller
@RestController
//Mapeia as rotas
@RequestMapping("/admins")
public class AdminController {

    private AdminRepository repository; // Injetando a ferramenta que fala com o banco

    //GET
    @GetMapping("/boasvinda")
    public String boasVindas() {
        return "Bem-vindo ao sistema de Administração!";
    }

    // Adicionar admin
    @PostMapping
    public AdminModel create(@RequestBody AdminModel admin) {
//        return "Admin created successfully";
        return repository.save(admin);
    }

    // Mostrar todos os admins
    @GetMapping
    public String findAll() {
        return "List of all admins";
    }

    // Procurar Admin por ID
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id) {
        return "listarAdminID";
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

package dev.blog.com.blog.Admins;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Essa anotation diz que essa classe é um controller
@RestController
//Mapeia as rotas
@RequestMapping
public class AdminController {

    //GET
    @GetMapping("/boasvinda")
    public String boasVindas() {
        return "boasVindas";
    }
}

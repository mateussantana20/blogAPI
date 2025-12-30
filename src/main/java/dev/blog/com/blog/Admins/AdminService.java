package dev.blog.com.blog.Admins;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {
    private final AdminRepository repository; // Injetando a ferramenta que fala com o banco
    public AdminService(AdminRepository repository) {
        this.repository = repository;
    }

    //Listar todos os ninjas
    public List<AdminModel> findAll () {
        return repository.findAll();
    }

    //Criar
    public AdminModel create(AdminModel admin) {
        return repository.save(admin);
    }

    // Listar por iD
    public AdminModel findById (Long id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteById (Long id) {
        repository.deleteById(id);
    }

}

package dev.blog.com.blog.config;

import dev.blog.com.blog.Admins.AdminModel;
import dev.blog.com.blog.Admins.AdminRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdminRepository repository;

    public UserDetailsServiceImpl(AdminRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AdminModel admin = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        return User.builder()
                .username(admin.getEmail())
                .password(admin.getPassword()) // Aqui ele pega a senha com hash do banco
                .roles("ADMIN")
                .build();
    }
}
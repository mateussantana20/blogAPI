package dev.blog.com.blog.Admins;

public record AdminDTO(
        Long id,
        String name,
        String email, // <--- ADICIONADO
        String profilePicture,
        String bio
) {
    public AdminDTO(AdminModel admin) {
        this(
                admin.getId(),
                admin.getName(),
                admin.getEmail(), // <--- ADICIONADO
                admin.getProfilePicture(),
                admin.getBio()
        );
    }
}
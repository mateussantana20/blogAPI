package dev.blog.com.blog.Admins;

public record AdminDTO(
        Long id,
        String name,
        String profilePicture,
        String bio
) {
    public AdminDTO(AdminModel admin) {
        this(
                admin.getId(),
                admin.getName(),
                admin.getProfilePicture(),
                admin.getBio()
        );
    }
}
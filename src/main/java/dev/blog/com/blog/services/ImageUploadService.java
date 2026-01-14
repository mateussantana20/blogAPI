package dev.blog.com.blog.services;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class ImageUploadService {
    private final Cloudinary cloudinary;

    public ImageUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file, String folderName) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", "escola/" + folderName));
        return uploadResult.get("secure_url").toString();
    }

    public void deleteImage(String imageUrl, String folderName) throws IOException {
        String publicId = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));
        String fullPublicId = "escola/" + folderName + "/" + publicId;
        cloudinary.uploader().destroy(fullPublicId, ObjectUtils.emptyMap());
    }

    public String uploadProfilePicture(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "escola/perfil",
                        "transformation", "w_200,h_200,c_fill,g_face" // Corta em 200x200 focando no rosto
                ));
        return uploadResult.get("secure_url").toString();
    }
}
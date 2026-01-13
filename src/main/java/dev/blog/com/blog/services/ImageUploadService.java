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

    public void deleteImage(String imageUrl) throws IOException {
        String publicId = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));
        String fullPublicId = "escola/posts/" + publicId;
        cloudinary.uploader().destroy(fullPublicId, ObjectUtils.emptyMap());
    }
}
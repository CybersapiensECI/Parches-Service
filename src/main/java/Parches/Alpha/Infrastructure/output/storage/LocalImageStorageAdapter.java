package Parches.Alpha.Infrastructure.output.storage;

import Parches.Alpha.Aplication.ports.ImageStoragePort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Component
public class LocalImageStorageAdapter implements ImageStoragePort {

    @Override
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String originalFilename = file.getOriginalFilename();
        String extension = "jpg";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }
        return "/uploads/" + UUID.randomUUID().toString() + "." + extension;
    }
}

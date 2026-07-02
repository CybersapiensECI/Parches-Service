package Parches.Alpha.Aplication.ports;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStoragePort {
    String store(MultipartFile file);
}

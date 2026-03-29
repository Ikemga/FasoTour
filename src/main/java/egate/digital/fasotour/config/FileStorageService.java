package egate.digital.fasotour.config;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadDir;

    public FileStorageService(FileStorageConfig config) throws IOException {
        this.uploadDir = config.getUploadDir();
        Files.createDirectories(uploadDir);
    }

    public String save(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("Fichier invalide");
            }

            String originalName = file.getOriginalFilename();
            if (originalName == null) {
                originalName = "file";
            }

            String cleanName = originalName
                    .replaceAll("\\s+", "_")
                    .replaceAll("[^a-zA-Z0-9._-]", "");

            String filename = UUID.randomUUID() + "_" + cleanName;

            Path target = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            return "/api/fasotour/uploads/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Erreur sauvegarde : " + e.getMessage(), e);
        }
    }
}
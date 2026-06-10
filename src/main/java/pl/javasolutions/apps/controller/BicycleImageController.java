package pl.javasolutions.apps.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.javasolutions.apps.exception.ResourceNotFoundException;
import pl.javasolutions.apps.repository.BicycleRepository;
import pl.javasolutions.apps.repository.model.BicycleEntity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/bicycles/{id}/image")
public class BicycleImageController {

    private final BicycleRepository bicycleRepository;
    private final Path uploadDir;

    public BicycleImageController(BicycleRepository bicycleRepository,
                                   @Value("${app.upload-dir:uploads}") String uploadDirPath) {
        this.bicycleRepository = bicycleRepository;
        this.uploadDir = Paths.get(uploadDirPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create upload directory", e);
        }
    }

    @PostMapping
    public ResponseEntity<String> upload(@PathVariable Long id,
                                          @RequestParam("file") MultipartFile file) throws IOException {
        BicycleEntity bicycle = bicycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bicycle", id));

        String filename = "bicycle-" + id + "-" + file.getOriginalFilename();
        Path targetPath = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        bicycle.setImagePath(filename);
        bicycleRepository.save(bicycle);

        return ResponseEntity.ok("Image uploaded: " + filename);
    }

    @GetMapping
    public ResponseEntity<Resource> download(@PathVariable Long id) throws MalformedURLException {
        BicycleEntity bicycle = bicycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bicycle", id));

        if (bicycle.getImagePath() == null) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = uploadDir.resolve(bicycle.getImagePath()).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + bicycle.getImagePath() + "\"")
                .body(resource);
    }
}


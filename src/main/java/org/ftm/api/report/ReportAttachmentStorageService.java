package org.ftm.api.report;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ReportAttachmentStorageService {

    private final Path rootDirectory;

    public ReportAttachmentStorageService(
            @Value("${app.storage.reports.directory}")
            String directory
    ) throws IOException{
        this.rootDirectory = Path.of(directory)
                .toAbsolutePath()
                .normalize();
        Files.createDirectories(this.rootDirectory);
    }

    public String store(MultipartFile file) throws IOException {
        Path directory = Paths.get(rootDirectory.toUri()).toAbsolutePath().normalize();
        Files.createDirectories(directory);

        String storageKey = UUID.randomUUID().toString();
        Path destination = directory.resolve(storageKey).normalize();

        if (!destination.startsWith(directory)) {
            throw new IOException("Invalid storage path");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(
                    inputStream,
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }

        return storageKey;
    }

    public Path resolve(String storageKey) {
        UUID.fromString(storageKey);

        Path resolved = rootDirectory
                .resolve(storageKey)
                .normalize();

        if (!resolved.startsWith(rootDirectory)) {
            throw new IllegalArgumentException("Invalid storage key");
        }

        return resolved;
    }

    public InputStream load(String storageKey) throws IOException {
        Path path = resolve(storageKey);

        if(!Files.isRegularFile(path)) {
            throw new NoSuchFileException(storageKey);
        }

        return Files.newInputStream(path);
    }

    public void delete(String storageKey) throws IOException {
        Files.deleteIfExists(resolve(storageKey));
    }
}

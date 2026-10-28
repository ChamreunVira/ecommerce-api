package com.kh.vira_dev.ecommerceapi.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class FileUploadUtils {

    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "webp");

    @Value("${UPLOAD_DIR:uploads/products}")
    private String uploadDirectory;

    @Value("${STORAGE_DRIVER:local}")
    private String storageDriver;

    @Value("${AWS_S3_BUCKET:}")
    private String bucket;

    private final ObjectProvider<S3Client> s3ClientProvider;

    public FileUploadUtils(ObjectProvider<S3Client> s3ClientProvider) {
        this.s3ClientProvider = s3ClientProvider;
    }

    public String upload(MultipartFile file) {
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename(), "File name is required");
        int extensionSeparator = originalFilename.lastIndexOf('.');
        if (extensionSeparator < 0 || extensionSeparator == originalFilename.length() - 1) {
            throw new IllegalArgumentException("Invalid file extension");
        }

        String extension = originalFilename.substring(extensionSeparator + 1).toLowerCase(Locale.ROOT);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Invalid file extension");
        }

        String uniqueFileName = UUID.randomUUID() + "." + extension;
        if (isS3Enabled()) {
            return uploadToS3(file, uniqueFileName);
        }
        return uploadLocally(file, uniqueFileName);
    }

    public void delete(String storedReference) {
        if (storedReference == null || storedReference.isBlank()) {
            return;
        }

        if (isS3Enabled()) {
            String key = keyFromReference(storedReference);
            if (key != null) {
                requireS3Client().deleteObject(DeleteObjectRequest.builder()
                        .bucket(requireBucket())
                        .key(key)
                        .build());
            }
            return;
        }

        try {
            Path uploadPath = Paths.get(uploadDirectory).toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(storedReference).normalize();
            if (!filePath.startsWith(uploadPath)) {
                throw new IllegalArgumentException("Invalid stored file reference");
            }
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("File delete error", e);
        }
    }

    private String uploadLocally(MultipartFile file, String fileName) {
        try {
            Path uploadPath = Paths.get(uploadDirectory).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(fileName).normalize();
            if (!filePath.startsWith(uploadPath)) {
                throw new IllegalArgumentException("Invalid file name");
            }
            file.transferTo(filePath);
            return fileName;
        } catch (IOException e) {
            log.error("Local file upload failed", e);
            throw new RuntimeException("File upload error", e);
        }
    }

    private String uploadToS3(MultipartFile file, String fileName) {
        String key = "uploads/" + fileName;
        S3Client s3 = requireS3Client();
        String bucketName = requireBucket();

        try {
            String contentType = file.getContentType();
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType == null ? "application/octet-stream" : contentType)
                    .build();
            s3.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return s3.utilities().getUrl(GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build()).toExternalForm();
        } catch (IOException e) {
            log.error("S3 file upload failed", e);
            throw new RuntimeException("File upload error", e);
        }
    }

    private String keyFromReference(String reference) {
        if (!reference.startsWith("http://") && !reference.startsWith("https://")) {
            return reference;
        }

        URI uri = URI.create(reference);
        String path = uri.getPath();
        if (path == null || path.length() < 2) {
            return null;
        }

        String key = path.substring(1);
        if (uri.getHost() == null || !uri.getHost().startsWith(requireBucket() + ".")) {
            String bucketPrefix = requireBucket() + "/";
            if (key.startsWith(bucketPrefix)) {
                key = key.substring(bucketPrefix.length());
            }
        }
        return key;
    }

    private boolean isS3Enabled() {
        return "s3".equalsIgnoreCase(storageDriver);
    }

    private S3Client requireS3Client() {
        S3Client client = s3ClientProvider.getIfAvailable();
        if (client == null) {
            throw new IllegalStateException("S3 storage is enabled but the S3 client is unavailable");
        }
        return client;
    }

    private String requireBucket() {
        if (bucket == null || bucket.isBlank()) {
            throw new IllegalStateException("AWS_S3_BUCKET must be configured when STORAGE_DRIVER=s3");
        }
        return bucket;
    }
}

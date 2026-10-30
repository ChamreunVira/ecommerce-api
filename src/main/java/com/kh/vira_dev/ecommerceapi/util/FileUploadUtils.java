package com.kh.vira_dev.ecommerceapi.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
            uploadToS3(file, uniqueFileName);
            return uniqueFileName;
        }
        return uploadLocally(file, uniqueFileName);
    }

    public ProductImage read(String filename) {
        validateFilename(filename);
        if (isS3Enabled()) {
            try {
                ResponseInputStream<GetObjectResponse> stream = requireS3Client().getObject(GetObjectRequest.builder()
                        .bucket(requireBucket())
                        .key("uploads/" + filename)
                        .build());
                GetObjectResponse metadata = stream.response();
                return new ProductImage(stream, contentType(metadata.contentType(), filename), metadata.contentLength());
            } catch (S3Exception e) {
                if (e.statusCode() == 404 || (e.awsErrorDetails() != null
                        && "NoSuchKey".equals(e.awsErrorDetails().errorCode()))) {
                    throw new ProductImageNotFoundException();
                }
                throw e;
            }
        }

        try {
            Path uploadPath = Paths.get(uploadDirectory).toAbsolutePath().normalize();
            Path imagePath = uploadPath.resolve(filename).normalize();
            if (!imagePath.startsWith(uploadPath) || !Files.isRegularFile(imagePath)) {
                throw new ProductImageNotFoundException();
            }
            return new ProductImage(Files.newInputStream(imagePath, StandardOpenOption.READ),
                    contentType(Files.probeContentType(imagePath), filename), Files.size(imagePath));
        } catch (IOException e) {
            throw new RuntimeException("Image storage unavailable", e);
        }
    }

    public String toImageFilename(String storedReference) {
        if (storedReference == null || storedReference.isBlank()) return storedReference;
        String reference = storedReference.trim();
        if (reference.startsWith("data:") || reference.startsWith("blob:")) return storedReference;
        if (reference.startsWith("http://") || reference.startsWith("https://")) {
            try {
                URI uri = URI.create(reference);
                String host = uri.getHost();
                String path = uri.getPath();
                boolean awsS3Host = host != null && host.endsWith(".amazonaws.com");
                boolean configuredBucketHost = host != null && bucket != null && !bucket.isBlank()
                        && host.startsWith(bucket + ".");
                boolean legacyUploadPath = path != null && path.contains("/uploads/");
                if (!awsS3Host && !configuredBucketHost && !legacyUploadPath) return storedReference;
                reference = path;
            } catch (IllegalArgumentException e) {
                return storedReference;
            }
        }
        reference = reference.replace('\\', '/');
        int slash = reference.lastIndexOf('/');
        return slash >= 0 ? reference.substring(slash + 1) : reference;
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

    private void uploadToS3(MultipartFile file, String fileName) {
        String key = "uploads/" + fileName;
        S3Client s3 = requireS3Client();
        String bucketName = requireBucket();

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType(null, fileName))
                    .build();
            s3.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            log.error("S3 file upload failed", e);
            throw new RuntimeException("File upload error", e);
        }
    }

    private String keyFromReference(String reference) {
        String value = reference;
        if (value.startsWith("http://") || value.startsWith("https://")) {
            URI uri = URI.create(value);
            String path = uri.getPath();
            if (path == null || path.length() < 2) return null;
            value = path.substring(1);
            String bucketPrefix = requireBucket() + "/";
            if (value.startsWith(bucketPrefix)) value = value.substring(bucketPrefix.length());
        }
        value = value.replace('\\', '/');
        if (!value.startsWith("uploads/")) value = "uploads/" + value.substring(value.lastIndexOf('/') + 1);
        return value;
    }

    private void validateFilename(String filename) {
        if (filename == null || !filename.matches("[A-Za-z0-9][A-Za-z0-9._-]{0,179}\\.(?:jpg|jpeg|png|webp)")) {
            throw new IllegalArgumentException("Invalid image filename");
        }
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            throw new IllegalArgumentException("Invalid image filename");
        }
    }

    private String contentType(String storedType, String filename) {
        if (storedType != null) {
            String normalizedType = storedType.toLowerCase(Locale.ROOT);
            if (List.of("image/jpeg", "image/png", "image/webp").contains(normalizedType)) {
                return normalizedType;
            }
        }
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "webp" -> "image/webp";
            default -> "application/octet-stream";
        };
    }

    public record ProductImage(InputStream stream, String contentType, long contentLength) implements AutoCloseable {
        @Override
        public void close() throws IOException { stream.close(); }
    }

    public static class ProductImageNotFoundException extends RuntimeException { }

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

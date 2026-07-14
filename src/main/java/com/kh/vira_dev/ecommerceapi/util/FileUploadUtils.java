package com.kh.vira_dev.ecommerceapi.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class FileUploadUtils {

    @Value("${uploads.dir}")
    private String UPLOAD_DIRECTORY;
    private final List<String> ALLOW_EXTENSIONS = List.of("jpg", "jpeg", "png" , "webp");

    public String upload(MultipartFile file) {
        try {

            var fileExtension = Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf(".") + 1);

            if(!ALLOW_EXTENSIONS.contains(fileExtension)) {
                throw new IllegalArgumentException("Invalid file extension");
            }

            Path uploadPath = Paths.get(UPLOAD_DIRECTORY);

            if(!Files.exists(uploadPath)) {
                Files.createDirectory(uploadPath);
            }

            String fileName = file.getOriginalFilename();
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            Path filPath = uploadPath.resolve(uniqueFileName)
                    .toAbsolutePath();

            Files.copy(file.getInputStream(), filPath, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;

        }catch (IOException e) {
            log.info("File upload error {}" , e.getLocalizedMessage());
            throw new RuntimeException("File upload error");
        }

    }

}

package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.util.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class FileController {

    private final FileUploadUtils fileUploadUtils;

    @GetMapping("/products/{filename:.+}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<StreamingResponseBody> readImage(@PathVariable String filename) {
        final FileUploadUtils.ProductImage image;
        try {
            image = fileUploadUtils.read(filename);
        } catch (FileUploadUtils.ProductImageNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.warn("Product image storage request failed");
            return ResponseEntity.internalServerError().build();
        }

        StreamingResponseBody body = output -> {
            try (image) {
                StreamUtils.copy(image.stream(), output);
            } catch (IOException e) {
                log.warn("Product image stream ended before completion");
                throw e;
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.contentType()));
        headers.setContentLength(image.contentLength());
        headers.setContentDisposition(ContentDisposition.inline().filename(filename).build());
        headers.setCacheControl(CacheControl.maxAge(Duration.ofDays(1)).cachePublic()
                .staleWhileRevalidate(Duration.ofDays(7)));
        headers.set("X-Content-Type-Options", "nosniff");
        return ResponseEntity.ok().headers(headers).body(body);
    }
}

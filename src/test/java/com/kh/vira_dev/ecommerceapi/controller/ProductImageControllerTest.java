package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.util.FileUploadUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductImageControllerTest {

    @TempDir
    Path uploadDirectory;

    private FileController controller;

    @BeforeEach
    void setUp() {
        ObjectProvider<S3Client> s3ClientProvider = new StaticListableBeanFactory()
                .getBeanProvider(S3Client.class);
        FileUploadUtils fileUploadUtils = new FileUploadUtils(s3ClientProvider);
        ReflectionTestUtils.setField(fileUploadUtils, "uploadDirectory", uploadDirectory.toString());
        ReflectionTestUtils.setField(fileUploadUtils, "storageDriver", "local");
        controller = new FileController(fileUploadUtils);
    }

    @Test
    void streamsImageBytesWithContentTypeAndCacheHeaders() throws Exception {
        byte[] expected = new byte[]{1, 2, 3, 4};
        Files.write(uploadDirectory.resolve("product.png"), expected);

        var response = controller.readImage("product.png");
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        ((StreamingResponseBody) response.getBody()).writeTo(actual);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("image/png", response.getHeaders().getContentType().toString());
        assertTrue(response.getHeaders().getCacheControl().contains("max-age=86400"));
        assertArrayEquals(expected, actual.toByteArray());
    }

    @Test
    void missingImageReturnsNotFound() {
        assertEquals(HttpStatus.NOT_FOUND, controller.readImage("missing.jpg").getStatusCode());
    }

    @Test
    void invalidFilenameReturnsBadRequest() {
        assertEquals(HttpStatus.BAD_REQUEST, controller.readImage("../secret.png").getStatusCode());
    }
}

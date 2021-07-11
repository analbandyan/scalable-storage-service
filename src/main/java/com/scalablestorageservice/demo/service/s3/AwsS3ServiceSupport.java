package com.scalablestorageservice.demo.service.s3;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.Upload;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class AwsS3ServiceSupport {

    private static final String FILE_ORIGINAL_NAME = "file-original-name";

    public static String generateObjectKey() {
        return UUID.randomUUID().toString();
    }

    public static ObjectMetadata constructObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setUserMetadata(Map.of(FILE_ORIGINAL_NAME, file.getOriginalFilename()));
        return objectMetadata;
    }

    public static String getFileOriginalName(S3Object s3Object) {
        return s3Object.getObjectMetadata().getUserMetaDataOf(FILE_ORIGINAL_NAME);
    }

    public static void doWithInputStream(MultipartFile file, Consumer<InputStream> isConsumer) {
        try (InputStream inputStream = file.getInputStream()) {
            isConsumer.accept(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get uploading file input stream", e);
        }
    }

    public static void waitForUpload(Upload uploadTask) {
        try {
            uploadTask.waitForUploadResult();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for upload.", e);
        }
    }
}

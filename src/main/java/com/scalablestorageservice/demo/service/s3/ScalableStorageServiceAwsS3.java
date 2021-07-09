package com.scalablestorageservice.demo.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.scalablestorageservice.demo.service.DownloadableFileData;
import com.scalablestorageservice.demo.service.ScalableStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.scalablestorageservice.demo.service.s3.AwsS3ServiceSupport.*;

@Slf4j
@Service
public class ScalableStorageServiceAwsS3 implements ScalableStorageService {

    private static final String BUCKET_NAME = "sss-s3-bucket";

    private final AmazonS3 amazonS3;
    private final TransferManager transferManager;

    public ScalableStorageServiceAwsS3(AmazonS3 amazonS3, TransferManager transferManager) {
        this.amazonS3 = amazonS3;
        this.transferManager = transferManager;
    }

    @Override
    public String upload(MultipartFile file) {
        makeSureBucketExists();
        String key = doUpload(file);
        log.info("Uploaded file key is {}", key);
        return key;
    }

    private void makeSureBucketExists() {
        boolean bucketExists = amazonS3.doesBucketExistV2(BUCKET_NAME);
        if (!bucketExists) {
            amazonS3.createBucket(BUCKET_NAME);
        }
    }

    private String doUpload(MultipartFile file) {
        String objectKey = generateObjectKey();
        transferManager.upload(BUCKET_NAME, objectKey, getInputStream(file), constructObjectMetadata(file));
        return objectKey;
    }

    @Override
    public DownloadableFileData download(String key) {
        S3Object s3Object = amazonS3.getObject(BUCKET_NAME, key);
        String fileOriginalName = getFileOriginalName(s3Object);
        return new DownloadableFileData(fileOriginalName, s3Object.getObjectContent());
    }

    @Override
    public void delete(String key) {
        amazonS3.deleteObject(BUCKET_NAME, key);
    }
}

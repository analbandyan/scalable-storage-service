package com.mq.demo.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class AwsS3MqWrapperService {

    private final static String BUCKET_NAME = "mq-demo-bucket";


    private final AmazonS3 amazonS3;
    private final TransferManager transferManager;

    public AwsS3MqWrapperService(AmazonS3 amazonS3, TransferManager transferManager) {
        this.amazonS3 = amazonS3;
        this.transferManager = transferManager;
    }

    public void upload(String id, MultipartFile file) {
        makeSureBucketExists();
        try {
            doUpload(id, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void makeSureBucketExists() {
        boolean doesBucketExist = amazonS3.doesBucketExistV2(BUCKET_NAME);
        if(!doesBucketExist) {
            amazonS3.createBucket(BUCKET_NAME);
        }
    }

    private void doUpload(String id, MultipartFile file) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        transferManager.upload(BUCKET_NAME, id, file.getInputStream(), objectMetadata);
    }

    public InputStream download(String id) {
        S3Object object = amazonS3.getObject(BUCKET_NAME, id);
        return object.getObjectContent();
    }


}

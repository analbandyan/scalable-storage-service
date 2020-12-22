package com.mq.demo.s3.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class ScalableStorageService {

    private final AwsS3MqWrapperService awsS3MqWrapperService;

    public ScalableStorageService(AwsS3MqWrapperService awsS3MqWrapperService) {
        this.awsS3MqWrapperService = awsS3MqWrapperService;
    }

    public void upload(String id, MultipartFile file) {
        awsS3MqWrapperService.upload(id, file);
    }

    public InputStream download(String id) {
        return awsS3MqWrapperService.download(id);
    }

}

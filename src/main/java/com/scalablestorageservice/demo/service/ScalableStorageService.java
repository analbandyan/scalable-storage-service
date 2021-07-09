package com.scalablestorageservice.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface ScalableStorageService {

    String upload(MultipartFile file);

    DownloadableFileData download(String key);

    void delete(String key);

}


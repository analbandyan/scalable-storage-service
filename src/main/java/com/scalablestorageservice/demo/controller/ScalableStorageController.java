package com.scalablestorageservice.demo.controller;

import com.scalablestorageservice.demo.service.DownloadableFileData;
import com.scalablestorageservice.demo.service.ScalableStorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/api/file")
public class ScalableStorageController {

    private final ScalableStorageService scalableStorageService;

    public ScalableStorageController(ScalableStorageService scalableStorageService) {
        this.scalableStorageService = scalableStorageService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        String key = scalableStorageService.upload(file);
        return ResponseEntity.ok(Map.of("key", key));
    }

    @GetMapping("/{key}")
    public ResponseEntity<StreamingResponseBody> download(@PathVariable("key") String key) {
        DownloadableFileData fileData = scalableStorageService.download(key);

        InputStream fileInputStream = fileData.getFileInputStream();

        StreamingResponseBody responseBody = constructStreamingResponseBody(fileInputStream);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = \"" + fileData.getFileName() + "\"")
                .body(responseBody);
    }

    private StreamingResponseBody constructStreamingResponseBody(InputStream fileInputStream) {
        return outputStream -> {
            fileInputStream.transferTo(outputStream);
            fileInputStream.close();
        };
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Object> delete(@PathVariable("key") String key) {
        scalableStorageService.delete(key);
        return ResponseEntity.noContent().build();
    }
}
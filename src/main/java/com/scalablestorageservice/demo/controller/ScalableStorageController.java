package com.scalablestorageservice.demo.controller;

import com.scalablestorageservice.demo.service.DownloadableFileData;
import com.scalablestorageservice.demo.service.ScalableStorageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<InputStreamResource> download(@PathVariable("key") String key) {
        DownloadableFileData fileData = scalableStorageService.download(key);
        InputStreamResource inputStreamResource = new InputStreamResource(fileData.getFileInputStream());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = \"" + fileData.getFileName() + "\"")
                .body(inputStreamResource);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Object> delete(@PathVariable("key") String key) {
        scalableStorageService.delete(key);
        return ResponseEntity.noContent().build();
    }
}